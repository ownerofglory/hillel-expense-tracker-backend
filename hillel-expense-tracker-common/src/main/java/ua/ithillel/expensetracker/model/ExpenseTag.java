package ua.ithillel.expensetracker.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseTag extends AbstractModel {
    private String name;
    private ExpenseTagColor color;
    private User user;
    private Set<Expense> expenses;
}
