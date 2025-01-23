package ua.ithillel.expensetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "t_category")
public class ExpenseCategory extends AbstractModel {
    private String name;

    @OneToMany
    @JoinColumn(name = "category_id")
    private Set<Expense> expenses;
}
