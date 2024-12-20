package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.ReportDTO;
import ua.ithillel.expensetracker.model.Report;

@Mapper
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    ReportDTO reportToReportDTO(Report report);
    Report reportDTOToReport(ReportDTO reportDTO);
}