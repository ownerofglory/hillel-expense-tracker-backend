package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.ExpenseTagDTO;
import ua.ithillel.expensetracker.model.Expense;
import ua.ithillel.expensetracker.model.ExpenseTag;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    ExpenseTagMapper TAG_MAPPER = Mappers.getMapper(ExpenseTagMapper.class);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "tagsToTagDTOs")
    ua.ithillel.expensetracker.dto.ExpenseDTO expenseToExpenseDTO(Expense expense);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "tagsDTOsToTags")
    Expense expenseDTOToExpense(ua.ithillel.expensetracker.dto.ExpenseDTO expenseDTO);

    @Named("tagsToTagDTOs")
    default Set<ExpenseTagDTO> tagsToTagDTOs(Set<ExpenseTag> expenseTags) {
        if (expenseTags == null) {
            expenseTags = new HashSet<>();
        }
        return expenseTags.stream()
                .map(TAG_MAPPER::toExpenseTagDto)
                .collect(Collectors.toSet());
    }

    @Named("tagsDTOsToTags")
    default Set<ExpenseTag> tagsDTOsToTags(Set<ExpenseTagDTO> expenseTagDTOs) {
        return expenseTagDTOs.stream()
                .map(TAG_MAPPER::toExpenseTagEntity)
                .collect(Collectors.toSet());
    }

}
