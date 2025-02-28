package ua.ithillel.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.expensetracker.dto.AuthDTO;
import ua.ithillel.expensetracker.dto.LoginDTO;
import ua.ithillel.expensetracker.dto.RegisterDTO;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.service.AuthService;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO registerDTO) {
        UserDTO userDTO = authService.registerUser(registerDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(@RequestBody LoginDTO loginDTO) {
        AuthDTO authDTO = authService.authenticateUser(loginDTO);
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }
}
