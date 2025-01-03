package ua.ithillel.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_tag")
public class ExpenseTag extends AbstractModel {
    private String name;
    @Transient
    private ExpenseTagColor color;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "t_tag_expense",
            inverseJoinColumns = @JoinColumn(name = "expense_id"),
            joinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Expense> expenses;
}
