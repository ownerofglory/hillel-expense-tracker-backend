package ua.ithillel.expensetracker.mapper;

import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.dto.ExpenseTagDTO;
import ua.ithillel.expensetracker.dto.ReportDTO;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.model.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestData {
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String EMAIL = "john.doe@example.com";
    public static final double AMOUNT = 100.0;
    public static final String CURRENCY = "UAH";
    public static final String DESCRIPTION = "This is a test expense";

    public static final Date START_DATE = new Date(2024,1, 1);
    public static final Date END_DATE = new Date(2024,2, 1);

    public static final String EXPENSE_CAT_NAME = "Utilities";
    public static final Set<ExpenseTagDTO> TAGS = Collections.emptySet();

    public static final User USER = new User(FIRST_NAME, LAST_NAME, EMAIL, new HashSet<>(), new HashSet<>(), new HashSet<>());
    public static final UserDTO USER_DTO = new UserDTO(FIRST_NAME, LAST_NAME, EMAIL);

    public static final ExpenseCategory EXPENSE_CATEGORY = new ExpenseCategory(EXPENSE_CAT_NAME, new HashSet<>());
    public static final ExpenseCategoryDTO EXPENSE_CATEGORY_DTO = new ExpenseCategoryDTO(EXPENSE_CAT_NAME);

    public static final ExpenseTagColor TAG_COLOR = ExpenseTagColor.CORAL;
    public static final String TAG_NAME = "test";
    public static final ExpenseTag TAG = new ExpenseTag(TAG_NAME, TAG_COLOR, null, null);
    public static final ExpenseTagDTO TAG_DTO = new ExpenseTagDTO(TAG_NAME, TAG_COLOR.getHexCode(), TAG_COLOR.getName());

    public static Report createReport() {
        Report report = new Report();
        report.setUser(USER);
        report.setReportType(ReportType.WEEKLY);
        report.setStartDate(START_DATE);
        report.setEndDate(END_DATE);
        report.setTopExpenseCategory(EXPENSE_CATEGORY);
        report.setAmountSpentInCategory(AMOUNT);
        return report;
    }

    public static ReportDTO createReportDTO() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setUser(USER_DTO);
        reportDTO.setReportType(ReportType.WEEKLY);
        reportDTO.setStartDate(START_DATE);
        reportDTO.setEndDate(END_DATE);
        reportDTO.setTopExpenseCategory(EXPENSE_CATEGORY_DTO);
        reportDTO.setAmountSpentInCategory(AMOUNT);
        return reportDTO;
    }
}
