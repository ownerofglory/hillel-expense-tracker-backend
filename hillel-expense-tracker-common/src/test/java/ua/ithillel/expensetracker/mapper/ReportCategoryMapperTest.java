package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.ReportCategoryDTO;
import ua.ithillel.expensetracker.dto.ReportDTO;
import ua.ithillel.expensetracker.model.Report;
import ua.ithillel.expensetracker.model.ReportCategory;

import static org.junit.jupiter.api.Assertions.*;

class ReportCategoryMapperTest {

    @Test
    void toReportCategoryDTO() {
        Report report = TestData.createReport();
        ReportCategory testReportCategory = new ReportCategory(report, TestData.EXPENSE_CATEGORY, TestData.AMOUNT);

        ReportCategoryDTO reportCategoryDTO = ReportCategoryMapper.INSTANCE.toReportCategoryDTO(testReportCategory);

        assertNotNull(reportCategoryDTO);
        assertEquals(report.getId(), reportCategoryDTO.getReport().getId());
        assertEquals(testReportCategory.getCategory().getName(), reportCategoryDTO.getCategory().getName());
        assertEquals(testReportCategory.getTotalSpent(), reportCategoryDTO.getTotalSpent());
    }

    @Test
    void toReportCategory() {
        ReportDTO report = TestData.createReportDTO();
        ReportCategoryDTO testReportCatDTO = new ReportCategoryDTO(report, TestData.EXPENSE_CATEGORY_DTO, TestData.AMOUNT);

        ReportCategory reportCategory = ReportCategoryMapper.INSTANCE.toReportCategory(testReportCatDTO);

        assertNotNull(reportCategory);
        assertEquals(report.getId(), reportCategory.getReport().getId());
        assertEquals(testReportCatDTO.getCategory().getName(), reportCategory.getCategory().getName());
        assertEquals(testReportCatDTO.getTotalSpent(), reportCategory.getTotalSpent());
    }
}