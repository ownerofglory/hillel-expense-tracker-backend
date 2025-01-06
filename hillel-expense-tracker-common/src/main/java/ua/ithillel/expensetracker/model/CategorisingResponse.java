package ua.ithillel.expensetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorisingResponse extends AbstractModel {
    private ExpenseCategory expenseCategory;
    private double amount;
    private String currency;
}
