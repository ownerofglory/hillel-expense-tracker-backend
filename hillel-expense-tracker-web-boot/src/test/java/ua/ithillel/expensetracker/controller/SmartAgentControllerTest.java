package ua.ithillel.expensetracker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptToolResponse;
import ua.ithillel.expensetracker.service.SmartAgentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SmartAgentControllerTest extends ControllerTestParent {
    @Mock
    private SmartAgentService smartAgentService;

    private SmartAgentController smartAgentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        smartAgentController = new SmartAgentController(smartAgentService);

        mockMvc = MockMvcBuilders.standaloneSetup(smartAgentController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void getChatCompletionTest_Ok() throws Exception {
        List<GptMessage> messages = new ArrayList<>();

        when(smartAgentService.getChatCompletionWithTools(anyList(), anyLong()))
                .thenReturn(Stream.of(new GptToolResponse(), new GptToolResponse(), new GptToolResponse(), new GptToolResponse()));

        String body = objectMapper.writeValueAsString(messages);

        mockMvc.perform(
                        post("/v1/agent?userId=1111")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.TEXT_EVENT_STREAM)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM));
    }
}
