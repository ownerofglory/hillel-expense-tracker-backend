package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.AuthDTO;
import ua.ithillel.expensetracker.dto.LoginDTO;
import ua.ithillel.expensetracker.dto.RegisterDTO;
import ua.ithillel.expensetracker.dto.UserDTO;

public interface AuthService {
    UserDTO registerUser(RegisterDTO registerDTO);
    AuthDTO authenticateUser(LoginDTO loginDTO);
    UserDTO getCurrentUser();
}
