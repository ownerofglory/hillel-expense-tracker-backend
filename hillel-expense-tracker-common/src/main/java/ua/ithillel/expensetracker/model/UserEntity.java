package ua.ithillel.expensetracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "user")
@Table(name = "t_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    private String email;
}
