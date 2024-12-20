package ua.ithillel.expensetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report extends AbstractModel {
    private User user;
    private ReportType reportType;
    private Date startDate;
    private Date endDate;
    private ExpenseCategory topExpenseCategory;
    private double amountSpentInCategory;
    private Set<ReportCategory> expensesByCategory;
}
