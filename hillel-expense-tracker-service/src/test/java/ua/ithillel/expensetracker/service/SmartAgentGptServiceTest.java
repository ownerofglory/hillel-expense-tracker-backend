package ua.ithillel.expensetracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.client.tool.GptToolChoice;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptMessageContent;
import ua.ithillel.expensetracker.model.GptToolResponse;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class SmartAgentGptServiceTest extends ServiceTestParent {
    @Mock
    private GPTClient gptClient;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ExpenseCategoryRepo expenseCategoryRepo;
    @Mock
    private ExpenseRepo expenseRepo;

    private SmartAgentGptService smartAgentGptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        smartAgentGptService = new SmartAgentGptService(
                gptClient, userRepo, expenseCategoryRepo, objectMapper);

        smartAgentGptService.setTools(tools);

    }
    
    @Test
    void getChatCompletionWithToolsTest_success() throws ExpenseTrackerPersistingException {
        GptMessage gptMessage = new GptMessage();
        gptMessage.setRole(GptMessage.ROLE_USER);
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
        gptMessageContent.setTextContent("");
        gptMessage.setContent(gptMessageContent);
        List<GptMessage> messages = List.of(gptMessage);
        Long userId = 1L;

        when(userRepo.find(anyLong())).thenReturn(Optional.of(new User()));
        when(gptClient.getChatCompletionWithToolsStream(anyList(), anyList())).thenReturn(Stream.of(new GptToolResponse()));

        Stream<GptToolResponse> chatCompletionWithTools = smartAgentGptService.getChatCompletionWithTools(messages, userId);

        assertNotNull(chatCompletionWithTools);
        chatCompletionWithTools.forEach(r -> assertNotNull(r));
    }

    @Test
    void getChatCompletionWithToolsTest_returnsTool() throws ExpenseTrackerPersistingException {
        GptMessage gptMessage = new GptMessage();
        gptMessage.setRole(GptMessage.ROLE_USER);
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
        gptMessageContent.setTextContent("");
        gptMessage.setContent(gptMessageContent);
        List<GptMessage> messages = List.of(gptMessage);
        Long userId = 1L;

        when(userRepo.find(anyLong())).thenReturn(Optional.of(new User()));
        GptToolResponse gptToolResponse = new GptToolResponse();
        GptToolChoice gptToolChoice = new GptToolChoice("id", "toolname", "{}", "{}");
        gptToolResponse.setTool(gptToolChoice);
        when(gptClient.getChatCompletionWithToolsStream(anyList(), anyList())).thenReturn(Stream.of(gptToolResponse));
        when(gptClient.getChatCompletion(anyList())).thenReturn(new GptToolResponse());

        Stream<GptToolResponse> chatCompletionWithTools = smartAgentGptService.getChatCompletionWithTools(messages, userId);

        assertNotNull(chatCompletionWithTools);
        chatCompletionWithTools.forEach(r -> {
            assertNotNull(r);
        });
    }
}
