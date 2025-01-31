package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

@Mapper
public interface ExpenseCategoryMapper {
    ExpenseCategoryMapper INSTANCE = Mappers.getMapper(ExpenseCategoryMapper.class);
    ExpenseMapper EXPENSE_MAPPER = Mappers.getMapper(ExpenseMapper.class);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    ExpenseCategoryDTO expenseCategoryToDTO(ExpenseCategory expenseCategory);
    ExpenseCategory DTOToExpenseCategory(ExpenseCategoryDTO expenseCategoryDTO);

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }
}
