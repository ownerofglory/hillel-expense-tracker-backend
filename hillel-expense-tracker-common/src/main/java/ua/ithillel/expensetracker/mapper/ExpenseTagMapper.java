package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.ExpenseTagDTO;
import ua.ithillel.expensetracker.model.ExpenseTag;

@Mapper
public interface ExpenseTagMapper {

    ExpenseTagMapper INSTANCE = Mappers.getMapper(ExpenseTagMapper.class);

    ExpenseTagDTO toExpenseTagDto(ExpenseTag expenseTag);
    ExpenseTag toExpenseTagEntity(ExpenseTagDTO expenseTagDTO);
}
