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

    @ManyToOne(cascade = CascadeType.MERGE)
    private ExpenseCategory category;
//
//    @Transient
//    private ExpenseCategory category;

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
