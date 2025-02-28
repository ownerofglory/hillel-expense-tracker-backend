package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUser();
    UserDTO getUserById(Long id);
}
