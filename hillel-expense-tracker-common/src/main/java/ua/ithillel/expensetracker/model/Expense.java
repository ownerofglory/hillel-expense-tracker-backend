package ua.ithillel.expensetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense extends AbstractModel {
    private double amount;
    private String description;
    private ExpenseCategory category;
    private User user;
    private Set<ExpenseTag> tags;
}
