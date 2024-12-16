package ua.ithillel.expensetracker.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseTag extends AbstractModel {

    private String name;

    private ExpenseTagColor color;
}
