package ua.ithillel.expensetracker.controller;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.NotFoundServiceException;
import ua.ithillel.expensetracker.service.ExpenseService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    }

    @Test
    void getAllTest_returnsNotFound() throws Exception {
        when(expenseService.getExpensesByUserId(anyLong())).thenThrow(NotFoundServiceException.class);

        mockMvc.perform(get("/v1/expenses?userId=1000"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void createExpenseTest_returnsOk() throws Exception {
        when(expenseService.createExpense(any())).thenReturn(new ExpenseDTO());

        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mockMvc.perform(
                    post("/v1/expenses")
                            .content(reqBody)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void createExpenseTest_returnsNotFound() throws Exception {
        when(expenseService.createExpense(any())).thenThrow(new NotFoundServiceException(""));

        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mockMvc.perform(
                        post("/v1/expenses")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void createExpenseTest_returnsInternalServerError() throws Exception {
        when(expenseService.createExpense(any())).thenThrow(new PersistenceException());

        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mockMvc.perform(
                        post("/v1/expenses")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void editExpenseTest_returnsOk() throws Exception {
        when(expenseService.updateExpense(anyLong(), any())).thenReturn(new ExpenseDTO());

        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setId(1L);
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mockMvc.perform(
                        put("/v1/expenses/1")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void editExpenseTest_returnsNotFound() throws Exception {
        when(expenseService.updateExpense(anyLong(), any())).thenThrow(new NotFoundServiceException(""));

        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setId(1L);
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mockMvc.perform(
                        put("/v1/expenses/1")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void editExpenseTest_returnsInternalServerError() throws Exception {
        when(expenseService.updateExpense(anyLong(), any())).thenThrow(new PersistenceException());

        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setId(1L);
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mockMvc.perform(
                        put("/v1/expenses/1")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void deleteExpenseTest_returnsOk() throws Exception {
        when(expenseService.deleteExpense(anyLong())).thenReturn(new ExpenseDTO());

        mockMvc.perform(
                        delete("/v1/expenses/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteExpenseTest_returnsNotFound() throws Exception {
        when(expenseService.deleteExpense(anyLong())).thenThrow(new NotFoundServiceException(""));

        mockMvc.perform(
                        delete("/v1/expenses/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}
