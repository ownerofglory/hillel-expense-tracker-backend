package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.model.ExpenseCategory;

import static org.junit.jupiter.api.Assertions.*;
class ExpenseCategoryMapperTest {

    @Test
    void expenseCategoryToDTO() {
        ExpenseCategoryDTO testExpenseCategoryDTO = ExpenseCategoryMapper.INSTANCE.expenseCategoryToDTO(TestData.EXPENSE_CATEGORY);

        assertNotNull(testExpenseCategoryDTO);
        assertEquals(TestData.EXPENSE_CAT_NAME, testExpenseCategoryDTO.getName());
    }

    @Test
    void DTOToExpenseCategory() {
        ExpenseCategory testExpenseCat = ExpenseCategoryMapper.INSTANCE.DTOToExpenseCategory(TestData.EXPENSE_CATEGORY_DTO);

        assertNotNull(testExpenseCat);
        assertEquals(TestData.EXPENSE_CAT_NAME, testExpenseCat.getName());
    }
}