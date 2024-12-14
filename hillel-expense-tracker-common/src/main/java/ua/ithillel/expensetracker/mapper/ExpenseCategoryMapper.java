package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.model.ExpenseCategory;

@Mapper
public interface ExpenseCategoryMapper {
    ExpenseCategoryMapper INSTANCE = Mappers.getMapper(ExpenseCategoryMapper.class);

    ExpenseCategoryDTO expenseCategoryToDTO(ExpenseCategory expenseCategory);
    ExpenseCategory DTOToExpenseCategory(ExpenseCategoryDTO expenseCategoryDTO);
}
