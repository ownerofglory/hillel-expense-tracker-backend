package ua.ithillel.expensetracker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.dto.SuggestionDTO;
import ua.ithillel.expensetracker.service.SmartExpenseService;

import java.io.BufferedInputStream;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SmartSuggestionControllerTest extends ControllerTestParent {
    @Mock
    private SmartExpenseService smartExpenseService;

    private SmartSuggestionController smartSuggestionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        smartSuggestionController = new SmartSuggestionController(smartExpenseService);

        mockMvc = MockMvcBuilders.standaloneSetup(smartSuggestionController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void categoriseExpenseTest_Ok() throws Exception {
        when(smartExpenseService.suggestExpenseByPrompt(anyString(), anyLong())).thenReturn(new ExpenseDTO());

        SuggestionDTO suggestionDTO = new SuggestionDTO();
        suggestionDTO.setPrompt("prompt");
        suggestionDTO.setUserId(1L);

        String reqBody = objectMapper.writeValueAsString(suggestionDTO);

        mockMvc.perform(post("/v1/suggest/expenses/prompt")
                        .content(reqBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void billScanTest_Ok() throws Exception {
        when(smartExpenseService.suggestExpenseByBillScan(any(), anyLong())).thenReturn(new ExpenseDTO());

        mockMvc.perform(multipart("/v1/suggest/expenses/bill-scan")
                        .file("file", getTestImage())
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private byte[] getTestImage() throws Exception {
        try (
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("test-img.jpg");
                BufferedInputStream bin = new BufferedInputStream(resourceAsStream)
        ) {
            return bin.readAllBytes();
        }
    }
}
