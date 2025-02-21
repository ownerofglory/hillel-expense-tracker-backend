package ua.ithillel.expensetracker.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String name;
    private String password;
}
