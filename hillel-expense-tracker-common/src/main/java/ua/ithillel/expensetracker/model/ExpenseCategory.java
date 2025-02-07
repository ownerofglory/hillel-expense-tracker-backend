package ua.ithillel.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_category")
@EqualsAndHashCode(exclude = {"expenses", "user"}, callSuper = false)
@ToString(exclude = {"expenses", "user"})
public class ExpenseCategory extends AbstractModel {
    private String name;

    @OneToMany
    @JoinColumn(name = "category_id")
    private Set<Expense> expenses;

    @ManyToOne
    private User user;
}
