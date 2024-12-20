package ua.ithillel.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.expensetracker.model.ReportType;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO extends AbstractDTO {
    private UserDTO user;
    private ReportType reportType;
    private Date startDate;
    private Date endDate;
    private ExpenseCategoryDTO topExpenseCategory;
    private double amountSpentInCategory;
    private Set<ReportCategoryDTO> expensesByCategory;
}
