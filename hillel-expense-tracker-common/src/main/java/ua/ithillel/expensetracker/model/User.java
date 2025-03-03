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
@Entity(name = "User")
@Table(name = "t_user")
public class User extends AbstractModel {
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    private String role;

    @Transient
    private Set<ExpenseTag> tags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<ExpenseCategory> categories;

    @OneToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REMOVE
    }, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<Expense> expenses;
}
