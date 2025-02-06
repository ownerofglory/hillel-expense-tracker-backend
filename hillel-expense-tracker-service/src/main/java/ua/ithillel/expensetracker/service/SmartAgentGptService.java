package ua.ithillel.expensetracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.client.tool.AgentToolType;
import ua.ithillel.expensetracker.client.tool.GptTool;
import ua.ithillel.expensetracker.client.tool.GptToolChoice;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.model.*;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.tools.GetExpensesBetweenDateToolSchema;
import ua.ithillel.expensetracker.tools.GetExpensesBetweenDatesArgs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class SmartAgentGptService implements SmartAgentService {
    private final GPTClient gptClient;
    private final UserRepo userRepo;
    private final ExpenseCategoryRepo expenseCategoryRepo;
    private final ExpenseRepo expenseRepo;
    private final ExpenseMapper expenseMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Stream<GptToolResponse> getChatCompletionWithTools(List<GptMessage> messages, Long userId) {
        try {
            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow(() -> new ServiceException("User not found"));

            List<ExpenseCategory> categories = expenseCategoryRepo.findByUser(existingUser);
            String categoriesStr = categories.stream()
                    .map(c -> MessageFormat.format("{0} | {1}", c.getId(), c.getName()))
                    .reduce("id | category_name\n", (acc, entry) -> acc + entry + "\n");

            // create chat completion context
            String userStr = MessageFormat.format("{0}: {1} {2}", existingUser.getId(), existingUser.getFirstname(), existingUser.getLastname());
            String gptContextTemplate = getContext();
            String currentDateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            String gptContext = MessageFormat.format(gptContextTemplate, userStr, currentDateTimeStr, categoriesStr);


            // prepare GPT messages
            GptMessage systemMessage = createGptTextMessage(GptMessage.ROLE_SYSTEM, gptContext);
            List<GptMessage> messagesToSend = new ArrayList<>();
            messagesToSend.add(systemMessage);
            messagesToSend.addAll(messages);


            GptTool<GetExpensesBetweenDateToolSchema> getExpensesBetweenDateToolGptTool = new GptTool<>();
            GetExpensesBetweenDateToolSchema getExpensesBetweenDateTool = new GetExpensesBetweenDateToolSchema("GetExpensesBetweenDateTool", "get the expenses between from and to dates for a given user");
            getExpensesBetweenDateToolGptTool.setName(getExpensesBetweenDateTool.getName());
            getExpensesBetweenDateToolGptTool.setDescription(getExpensesBetweenDateTool.getDescription());
            getExpensesBetweenDateToolGptTool.setParameters(getExpensesBetweenDateTool);

            List<GptTool<? extends AgentToolType>> tools = List.of(getExpensesBetweenDateToolGptTool);

            Stream<GptToolResponse> chatCompletionWithToolsStream = gptClient.getChatCompletionWithToolsStream(messagesToSend, tools);

            return chatCompletionWithToolsStream.map(resp -> processGptToolResponse(resp, messagesToSend));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ExpenseTrackerPersistingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Tools:
    private List<Expense> getExpensesBetweenDates(GetExpensesBetweenDatesArgs args) throws ExpenseTrackerPersistingException {
        Long userId = args.getUserId();
        User user = userRepo.find(userId).orElseThrow();

        return expenseRepo.findByUserBetweenDates(user, args.getFrom(), args.getTo());
    }

    private GptToolResponse processGptToolResponse(GptToolResponse resp, List<GptMessage> messagesToSend) {
        GptToolChoice tool = resp.getTool();
        if (tool != null) {
            try {
                String toolName = tool.getToolName();
                String args = tool.getArgs();

                if (toolName != null) {
                    switch (toolName) {
                        case "GetExpensesBetweenDateTool" -> {
                            GetExpensesBetweenDatesArgs funcArgs = objectMapper.readValue(args, GetExpensesBetweenDatesArgs.class);
                            return executeGetExpensesBetweenDateTool(funcArgs, tool,messagesToSend);
                        }
                    }
                }
            } catch (JsonProcessingException | ExpenseTrackerPersistingException e) {
                throw new RuntimeException(e);
            }
        }

        return resp;
    }

    private GptToolResponse executeGetExpensesBetweenDateTool(GetExpensesBetweenDatesArgs funcArgs, GptToolChoice tool, List<GptMessage> messagesToSend) throws ExpenseTrackerPersistingException, JsonProcessingException {
        List<Expense> expenses = getExpensesBetweenDates(funcArgs);
        List<ExpenseDTO> expenseDTOS = expenses.stream().map(expenseMapper::expenseToExpenseDTO).toList();
        String toolCallResult = objectMapper.writeValueAsString(expenseDTOS);

        GptMessage gptAssistantMessage = createGptToolRespMessage(tool.getToolRaw());

        GptMessage gptMessage = createGptToolResultMessage(tool.getId(), toolCallResult);

        List<GptMessage> resultMessages = new ArrayList<>(messagesToSend);
        resultMessages.add(gptAssistantMessage);
        resultMessages.add(gptMessage);

        GptResponse<String> chatCompletion = gptClient.getChatCompletion(resultMessages);
        GptToolResponse gptToolResponse = new GptToolResponse();
        gptToolResponse.setContent(chatCompletion.getContent());
        return gptToolResponse;
    }

    private GptMessage createGptTextMessage(String role, String text) {
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
        gptMessageContent.setTextContent(text);
        return new GptMessage(role, gptMessageContent);
    }

    private GptMessage createGptContextMessage(String role, String text) {
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
        gptMessageContent.setTextContent(text);
        return new GptMessage(role, gptMessageContent);
    }

    private GptMessage createGptToolRespMessage(String toolRaw) {
        GptMessageContent assistantContent = new GptMessageContent();
        assistantContent.setType(GptMessageContent.CONTENT_TYPE_TOOL_CALL);
        assistantContent.setToolRaw(toolRaw);
        return new GptMessage(GptMessage.ROLE_ASSISTANT, assistantContent);
    }

    private GptMessage createGptToolResultMessage(String toolId, String callResult) {
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TOOL_CALL_RESULT);
        gptMessageContent.setToolCallId(toolId);
        gptMessageContent.setToolCallResult(callResult);
        return new GptMessage(GptMessage.ROLE_TOOL, gptMessageContent);
    }

    private String getContext() throws IOException {
        try (
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("gpt-agent-context.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
        ) {
            return br.lines()
                    .reduce((acc, line) -> acc + line)
                    .orElse("");
        }
    }
}
