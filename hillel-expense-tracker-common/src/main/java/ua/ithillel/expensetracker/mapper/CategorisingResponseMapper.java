package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.CategorisingResponseDTO;
import ua.ithillel.expensetracker.model.CategorisingResponse;
import ua.ithillel.expensetracker.model.ExpenseCategory;

import java.util.HashSet;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategorisingResponseMapper {
    CategorisingResponseMapper INSTANCE = Mappers.getMapper(CategorisingResponseMapper.class);

    @Mapping(target = "expenseCategory", source = "categoryName", qualifiedByName = "nameToCategory")
    CategorisingResponse toCategorisingResponse(CategorisingResponseDTO categorisingResponseDTO);

    @Named("nameToCategory")
    default ExpenseCategory nameToCategory (String categoryName) {return new ExpenseCategory(categoryName, new HashSet<>(), null);}
}
