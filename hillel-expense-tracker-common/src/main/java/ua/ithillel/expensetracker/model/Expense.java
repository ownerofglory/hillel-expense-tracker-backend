package ua.ithillel.expensetracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_expense")
public class Expense extends AbstractModel {
    private double amount;
    private String description;

    @Transient
    private ExpenseCategory category;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "t_tag_expense",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")

    )
    private Set<ExpenseTag> tags;
}
