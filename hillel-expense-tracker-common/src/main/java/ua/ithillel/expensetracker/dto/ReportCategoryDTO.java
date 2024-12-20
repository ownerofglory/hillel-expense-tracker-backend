package ua.ithillel.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCategoryDTO extends AbstractDTO {
    private ReportDTO report;
    private ExpenseCategoryDTO category;
    private double totalSpent;
}