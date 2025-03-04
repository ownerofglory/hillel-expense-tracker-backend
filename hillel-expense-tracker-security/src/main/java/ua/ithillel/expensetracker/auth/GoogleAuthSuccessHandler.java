package ua.ithillel.expensetracker.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.model.security.HillelUserDetails;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.util.JwtUtil;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class GoogleAuthSuccessHandler implements AuthenticationSuccessHandler {
    private static final String SUCCESS_REDIRECT_URL = System.getenv("AUTH_SUCCESS_REDIRECT_URL");

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        String fullName = principal.getAttribute("name");

        User user = getOrCreateUser(email, fullName);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User could not be created.");
            return;
        }

        String token = jwtUtil.generateToken(new HillelUserDetails(user));
        response.sendRedirect(SUCCESS_REDIRECT_URL + "/" + token);
    }

    private User getOrCreateUser(String email, String fullName) {
        try {
            return userRepo.findByEmail(email).orElseGet(() -> createUser(email, fullName));
        } catch (ExpenseTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }

    private User createUser(String email, String fullName) {
        try {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setRole("ROLE_USER");

            String[] nameParts = fullName.split(" ", 2);
            newUser.setFirstname(nameParts.length > 0 ? nameParts[0] : "");
            newUser.setLastname(nameParts.length > 1 ? nameParts[1] : "");

            return userRepo.save(newUser).orElse(null);
        } catch (ExpenseTrackerPersistingException e) {
            return null;
        }
    }
}
