package ua.ithillel.expensetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.dto.AuthDTO;
import ua.ithillel.expensetracker.dto.LoginDTO;
import ua.ithillel.expensetracker.dto.RegisterDTO;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.exception.NotFoundServiceException;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.mapper.UserMapper;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.model.security.HillelUserDetails;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.util.JwtUtil;

import java.util.Optional;
import java.util.Set;

@Service
@Primary
@RequiredArgsConstructor
public class LocalAuthService implements AuthService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public UserDTO registerUser(RegisterDTO registerDTO) {
        try {
            String password = registerDTO.getPassword();
            // validate password

            String passwordHash = passwordEncoder.encode(password);
            User user = new User();
            user.setFirstname(registerDTO.getFirstName());
            user.setLastname(registerDTO.getLastName());
            user.setEmail(registerDTO.getEmail());
            user.setPasswordHash(passwordHash);

            user.setCategories(Set.of(
                    new ExpenseCategory("Food", Set.of(), user),
                    new ExpenseCategory("Transportation", Set.of(), user),
                    new ExpenseCategory("Entertainment", Set.of(), user),
                    new ExpenseCategory("Health", Set.of(), user)
            ));

            Optional<User> savedUserOpt = userRepo.save(user);

            User savedUser = savedUserOpt.orElseThrow(() -> new NotFoundServiceException("User not found"));

            return userMapper.userToUserDTO(savedUser);
        } catch (ExpenseTrackerPersistingException e) {
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    public AuthDTO authenticateUser(LoginDTO loginDTO) {
        try {
            String password = loginDTO.getPassword();
            String email = loginDTO.getEmail();

            Optional<User> byEmail = userRepo.findByEmail(email);
            User user = byEmail.get();


            UserDetails authAttempt = org.springframework.security.core.userdetails.User
                    .builder()
                    .username(email)
                    .password(password)
                    .authorities(Set.of(new SimpleGrantedAuthority(user.getRole())))
                    .build();


            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authAttempt, password)
            );

            HillelUserDetails principal = (HillelUserDetails) authenticate.getPrincipal();

            if (principal != null) {
                String principalPassword = principal.getPassword();

                boolean matches = passwordEncoder.matches(password, principalPassword);

                if (matches) {
                    String token = jwtUtil.generateToken(principal);

                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setToken(token);
                    authDTO.setUser(userMapper.userToUserDTO(user));

                    return authDTO;
                }
            }

            return null;
        } catch (ExpenseTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO getCurrentUser() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();

            Object principal = authentication.getPrincipal();

            Long userId = switch (principal) {
                case HillelUserDetails hillelPrincipal -> hillelPrincipal.getId();
                default -> throw new ServiceException("Anonymous user");
            };

            Optional<User> userOpt = userRepo.find(userId);

            User user = userOpt.orElseThrow(() -> new NotFoundServiceException("User not found"));

            return userMapper.userToUserDTO(user);
        } catch (ExpenseTrackerPersistingException e) {
            throw new NotFoundServiceException("User not found");
        }
    }
}
