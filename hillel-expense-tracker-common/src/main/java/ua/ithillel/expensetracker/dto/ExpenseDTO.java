package ua.ithillel.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO extends AbstractDTO {
    private double amount;
    private String description;
    private ExpenseCategoryDTO category;
    private UserDTO user;
    private Long userId;
    private Long categoryId;
    private Set<ExpenseTagDTO> tags = new HashSet<>();
}
