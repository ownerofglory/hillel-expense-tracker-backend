package ua.ithillel.expensetracker.mapper;

import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.ExpenseTagDTO;
import ua.ithillel.expensetracker.model.ExpenseTag;
import ua.ithillel.expensetracker.model.ExpenseTagColor;

import java.util.Arrays;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpenseTagMapper {
    ExpenseTagMapper INSTANCE = Mappers.getMapper(ExpenseTagMapper.class);

    @Mapping(target = "hexCode", source = "color", qualifiedByName = "colorToHexCode")
    @Mapping(target = "colorName", source = "color", qualifiedByName = "colorToColorName")
    ExpenseTagDTO toExpenseTagDto(ExpenseTag expenseTag);

    @Mapping(target = "color", source = "hexCode", qualifiedByName = "toExpenseTagColor")
    ExpenseTag toExpenseTagEntity(ExpenseTagDTO expenseTagDTO);

    @Named("colorToHexCode")
    default String colorToHexCode(ExpenseTagColor color) {
        if (color == null) {
            return "";
        }

        if (color.getHexCode() != null) {
            return color.getHexCode();
        }
        return "";
    }

    @Named("colorToColorName")
    default String colorToColorName(ExpenseTagColor color) {
        if (color == null) {
            return "";
        }
        return color.getName();
    }

    @Named("toExpenseTagColor")
    default ExpenseTagColor toExpenseTagColor(String hexCode) {
        return Arrays.stream(ExpenseTagColor.values())
                .filter(c -> c.getHexCode().equals(hexCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("unknown hex code: " + hexCode));
    }
}