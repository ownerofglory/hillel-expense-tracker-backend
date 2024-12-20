package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.ReportCategoryDTO;
import ua.ithillel.expensetracker.model.ReportCategory;

@Mapper
public interface ReportCategoryMapper {
    ReportCategoryMapper INSTANCE = Mappers.getMapper(ReportCategoryMapper.class);

    ReportCategoryDTO toReportCategoryDTO(ReportCategory reportCategory);
    ReportCategory toReportCategory(ReportCategoryDTO reportCategoryDTO);
}
