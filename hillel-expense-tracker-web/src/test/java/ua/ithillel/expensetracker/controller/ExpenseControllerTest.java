package ua.ithillel.expensetracker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ithillel.expensetracker.service.ExpenseService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExpenseControllerTest extends ControllerTestParent {
    @Mock
    private ExpenseService expenseService;

    private ExpenseController expenseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        expenseController = new ExpenseController(expenseService);

        mockMvc = MockMvcBuilders.standaloneSetup(expenseController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }


    @Test
    void getAllTest_returnsOk() throws Exception {
        when(expenseService.getExpensesByUserId(anyLong())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/v1/expenses?userId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        System.out.println();
    }
}
