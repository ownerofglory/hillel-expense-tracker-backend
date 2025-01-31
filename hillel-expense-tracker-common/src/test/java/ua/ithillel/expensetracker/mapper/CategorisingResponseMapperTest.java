package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.CategorisingResponseDTO;
import ua.ithillel.expensetracker.model.CategorisingResponse;
import ua.ithillel.expensetracker.model.ExpenseCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategorisingResponseMapperTest {

    @Test
    void toCategorisingResponse() {
        CategorisingResponseDTO testResponseDTO = new CategorisingResponseDTO(TestData.EXPENSE_CAT_NAME, TestData.AMOUNT, "", TestData.CURRENCY, false, "");

        CategorisingResponse response = CategorisingResponseMapper.INSTANCE.toCategorisingResponse(testResponseDTO);

        assertNotNull(response);
        assertEquals(response.getExpenseCategory().getName(), TestData.EXPENSE_CAT_NAME);
        assertEquals(response.getAmount(), TestData.AMOUNT);
        assertEquals(response.getCurrency(), TestData.CURRENCY);
    }

    @Test
    void nameToCategory() {
        ExpenseCategory expenseCategory = CategorisingResponseMapper.INSTANCE.nameToCategory(TestData.EXPENSE_CAT_NAME);

        assertNotNull(expenseCategory);
        assertEquals(expenseCategory.getName(), TestData.EXPENSE_CAT_NAME);
    }
}