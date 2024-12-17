package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.ExpenseTagDTO;
import ua.ithillel.expensetracker.model.ExpenseTag;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ua.ithillel.expensetracker.mapper.TestData.TAG;
import static ua.ithillel.expensetracker.mapper.TestData.TAG_DTO;

public class ExpenseTagMapperTest {
    private ExpenseTagMapper tagMapper;

    @BeforeEach
    void setUp() {
        tagMapper = ExpenseTagMapper.INSTANCE;
    }

    @Test
    void toExpenseTagDto() {
        ExpenseTagDTO expenseTagDto = tagMapper.toExpenseTagDto(TAG);
        assertNotNull(expenseTagDto);
        assertNotNull(expenseTagDto.getColorName());
        assertNotNull(expenseTagDto.getHexCode());
    }

    @Test
    void toExpenseTagEntity() {
        ExpenseTag expenseTagEntity = tagMapper.toExpenseTagEntity(TAG_DTO);
        assertNotNull(expenseTagEntity);
        assertNotNull(expenseTagEntity.getName());
        assertNotNull(expenseTagEntity.getColor());
    }
}
