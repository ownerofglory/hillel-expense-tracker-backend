package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.ReportDTO;
import ua.ithillel.expensetracker.model.Report;

import static org.junit.jupiter.api.Assertions.*;

class ReportMapperTest {

    @Test
    void reportToReportDTO() {
        Report testReport = TestData.createReport();
        ReportDTO reportDTO = ReportMapper.INSTANCE.reportToReportDTO(testReport);

        assertNotNull(reportDTO);
        assertEquals(testReport.getId(), reportDTO.getId());
        assertEquals(TestData.EXPENSE_CATEGORY.getName(), reportDTO.getTopExpenseCategory().getName());
        assertEquals(TestData.AMOUNT, reportDTO.getAmountSpentInCategory());
    }

    @Test
    void reportDTOToReport() {
        ReportDTO testReportDTO = TestData.createReportDTO();
        Report report = ReportMapper.INSTANCE.reportDTOToReport(testReportDTO);

        assertNotNull(report);
        assertEquals(testReportDTO.getId(), report.getId());
        assertEquals(TestData.EXPENSE_CATEGORY.getName(), report.getTopExpenseCategory().getName());
        assertEquals(TestData.AMOUNT, report.getAmountSpentInCategory());
    }
}