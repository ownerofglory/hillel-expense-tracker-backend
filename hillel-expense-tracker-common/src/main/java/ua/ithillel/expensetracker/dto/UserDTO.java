package ua.ithillel.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.expensetracker.model.ExpenseTag;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AbstractDTO {
    private String firstname;
    private String lastname;
    private String email;
}
