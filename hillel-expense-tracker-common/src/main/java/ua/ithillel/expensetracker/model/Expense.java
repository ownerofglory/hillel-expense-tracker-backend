package ua.ithillel.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_expense")
@ToString(exclude = {"tags", "user", "category"})
public class Expense extends AbstractModel {
    private double amount;
    private String description;

    @ManyToOne
    private ExpenseCategory category;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "t_tag_expense",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")

    )
    private Set<ExpenseTag> tags;
}
