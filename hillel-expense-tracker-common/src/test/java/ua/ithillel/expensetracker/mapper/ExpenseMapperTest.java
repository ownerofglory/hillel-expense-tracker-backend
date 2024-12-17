package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.model.Expense;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
class ExpenseMapperTest {

    @Test
    void expenseToExpenseDTO() {
        Expense testExpense = new Expense(TestData.AMOUNT, TestData.DESCRIPTION, TestData.EXPENSE_CATEGORY, TestData.USER, new HashSet<>());

        ExpenseDTO testExpenseDTO = ExpenseMapper.INSTANCE.expenseToExpenseDTO(testExpense);

        assertNotNull(testExpenseDTO);
        assertEquals(TestData.AMOUNT, testExpenseDTO.getAmount());
        assertEquals(TestData.DESCRIPTION, testExpenseDTO.getDescription());
        assertEquals(TestData.EXPENSE_CATEGORY.getName(), testExpenseDTO.getCategory().getName());
        assertEquals(TestData.USER.getId(), testExpenseDTO.getUser().getId());
    }

    @Test
    void expenseDTOToExpense() {
        ExpenseDTO testExpenseDTO = new ExpenseDTO(TestData.AMOUNT, TestData.DESCRIPTION, TestData.EXPENSE_CATEGORY_DTO, TestData.USER_DTO, new HashSet<>());

        Expense testExpense = ExpenseMapper.INSTANCE.expenseDTOToExpense(testExpenseDTO);

        assertNotNull(testExpense);
        assertEquals(TestData.AMOUNT, testExpense.getAmount());
        assertEquals(TestData.DESCRIPTION, testExpense.getDescription());
        assertEquals(TestData.EXPENSE_CATEGORY.getName(), testExpense.getCategory().getName());
        assertEquals(TestData.USER.getId(), testExpense.getUser().getId());
    }
}