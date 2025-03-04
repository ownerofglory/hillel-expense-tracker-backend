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
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GoogleAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final static String SUCCESS_REDIRECT_URL = System.getenv("AUTH_SUCCESS_REDIRECT_URL");

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        String fullName = principal.getAttribute("name");
        User user = null;

        try {
            Optional<User> userOpt = userRepo.findByEmail(email);
            user = userOpt.orElseGet(() -> {
                try {
                    User newUser = new User();
                    newUser.setEmail(email);
                    String[] nameLastname = fullName.split(" ");
                    newUser.setFirstname(nameLastname[0]);
                    newUser.setLastname(nameLastname[1]);

                    Optional<User> saved = userRepo.save(newUser);

                    return saved.get();

                } catch (ExpenseTrackerPersistingException e) {
                    return null;
                }
            });

        } catch (Exception e) {

        } finally {
            String token = jwtUtil.generateToken(new HillelUserDetails(user));

            response.sendRedirect(SUCCESS_REDIRECT_URL + "?jwt=" + token);
        }
    }
}
