package ua.ithillel.expensetracker.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import ua.ithillel.expensetracker.dto.ExpenseDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExpenseE2ETest extends E2ETestParent {
    @BeforeEach
    void setUp() {
        System.out.println();
    }

    @Test
    @WithUserDetails("john.doe@example.com")
    void createAndGetExpenseTest() throws Exception {
        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        MvcResult mvcResult = mvc.perform(
                        post("/v1/expenses")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        System.out.println();
    }

    @Test
    @WithUserDetails("john.doe@example.com")
    void createAndGetExpenseTest_forDifferentUserForbidden() throws Exception {
        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(2L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mvc.perform(
                        post("/v1/expenses")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void createAndGetExpenseTest_anonymousUserUnauthorized() throws Exception {
        ExpenseDTO testExpense = new ExpenseDTO();
        testExpense.setDescription("description");
        testExpense.setAmount(10.00);
        testExpense.setUserId(1L);
        testExpense.setCategoryId(1L);

        String reqBody = objectMapper.writeValueAsString(testExpense);

        mvc.perform(
                        post("/v1/expenses")
                                .content(reqBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }
}
