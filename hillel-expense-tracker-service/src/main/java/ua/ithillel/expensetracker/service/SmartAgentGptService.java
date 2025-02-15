package ua.ithillel.expensetracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.client.tool.AgentToolType;
import ua.ithillel.expensetracker.client.tool.GptTool;
import ua.ithillel.expensetracker.client.tool.GptToolChoice;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.exception.NotFoundServiceException;
import ua.ithillel.expensetracker.exception.ServiceErrorException;
import ua.ithillel.expensetracker.model.*;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.tools.definition.AgentToolDef;
import ua.ithillel.expensetracker.tools.exception.ToolExecException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Setter
public class SmartAgentGptService implements SmartAgentService {
    private final GPTClient gptClient;
    private final UserRepo userRepo;
    private final ExpenseCategoryRepo expenseCategoryRepo;
    private final ObjectMapper objectMapper;

    @Qualifier("agentTools")
    @Autowired
    private Map<String, AgentToolDef> tools;

    @Override
    public Stream<GptToolResponse> getChatCompletionWithTools(List<GptMessage> messages, Long userId) {
        try {
            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow(() -> new NotFoundServiceException("User not found"));

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


            List<GptTool<AgentToolType>> toolList = tools.values().stream().map(tool -> {
                GptTool<AgentToolType> objectGptTool = new GptTool<>();
                objectGptTool.setName(tool.getSchema().getName());
                objectGptTool.setDescription(tool.getSchema().getDescription());
                objectGptTool.setParameters(tool.getSchema());
                return objectGptTool;
            }).toList();

            Stream<GptToolResponse> chatCompletionWithToolsStream = gptClient.getChatCompletionWithToolsStream(messagesToSend, toolList);

            Stream<GptToolResponse> resultStream = chatCompletionWithToolsStream.map(resp -> processGptToolResponse(resp, messagesToSend));
            return Stream.concat(resultStream, Stream.of(new GptToolResponse()));
        }  catch (ExpenseTrackerPersistingException e) {
            throw new NotFoundServiceException("User not found");
        } catch (IOException e) {
            throw new ServiceErrorException("Service error: " + e.getMessage());
        }
    }


    private GptToolResponse processGptToolResponse(GptToolResponse resp, List<GptMessage> messagesToSend) {
        GptToolChoice tool = resp.getTool();
        if (tool != null) {
            try {
                String toolName = tool.getToolName();
                String args = tool.getArgs();

                if (toolName != null) {
                    AgentToolDef agentToolDef = tools.get(toolName);
                    if (agentToolDef == null) {
                        GptToolChoice gptToolChoice = new GptToolChoice("", "", "", "");
                        GptToolResponse gptToolResponse = new GptToolResponse();
                        gptToolResponse.setTool(gptToolChoice);

                        return gptToolResponse;
                    }
                    Object resultObj = agentToolDef.execute(args);
                    String toolCallResult = objectMapper.writeValueAsString(resultObj);


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
            } catch (JsonProcessingException e) {
                throw new ServiceErrorException("Unable to process tool response: " + e.getMessage());
            } catch (ToolExecException e) {
                throw new RuntimeException(e);
            }
        }

        return resp;
    }

    private Stream<GptToolResponse> processGptToolResponseStream(GptToolResponse resp, List<GptMessage> messagesToSend) {
        GptToolChoice tool = resp.getTool();
        if (tool != null) {
            try {
                String toolName = tool.getToolName();
                String args = tool.getArgs();

                if (toolName != null) {
                    AgentToolDef agentToolDef = tools.get(toolName);
                    if (agentToolDef == null) {
                        GptToolChoice gptToolChoice = new GptToolChoice("", "", "", "");
                        GptToolResponse gptToolResponse = new GptToolResponse();
                        gptToolResponse.setTool(gptToolChoice);

                        return Stream.of(gptToolResponse, null);
                    }
                    Object resultObj = agentToolDef.execute(args);
                    String toolCallResult = objectMapper.writeValueAsString(resultObj);


                    GptMessage gptAssistantMessage = createGptToolRespMessage(tool.getToolRaw());

                    GptMessage gptMessage = createGptToolResultMessage(tool.getId(), toolCallResult);

                    List<GptMessage> resultMessages = new ArrayList<>(messagesToSend);
                    resultMessages.add(gptAssistantMessage);
                    resultMessages.add(gptMessage);

                    GptResponse<String> chatCompletion = gptClient.getChatCompletion(resultMessages);
                    GptToolResponse gptToolResponse = new GptToolResponse();
                    gptToolResponse.setContent(chatCompletion.getContent());

                    return Stream.of(gptToolResponse, null);
                }
            } catch (JsonProcessingException e) {
                throw new ServiceErrorException("Unable to process tool response: " + e.getMessage());
            } catch (ToolExecException e) {
                throw new RuntimeException(e);
            }
        }

        return Stream.of(resp, null);
    }


    private GptMessage createGptTextMessage(String role, String text) {
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
