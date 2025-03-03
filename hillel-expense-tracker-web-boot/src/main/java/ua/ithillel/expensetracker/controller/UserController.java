package ua.ithillel.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.service.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDTO getUsersById(@PathVariable Long id) {
        UserDTO userById = userService.getUserById(id);
        return userById;
    }
}
