package ua.ithillel.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO extends AbstractDTO {
    private double amount;
    private String description;
    private ExpenseCategoryDTO category;
    private UserDTO user;
}
