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
public class User extends AbstractModel{
    private String firstname;
    private String lastname;
    private String email;
    private Set<ExpenseTag> tags;
    private Set<ExpenseCategory> categories;
    private Set<Expense> expenses;
}
