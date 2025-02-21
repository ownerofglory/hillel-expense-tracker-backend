package ua.ithillel.expensetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.model.security.HillelUserDetails;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.Optional;

@RequiredArgsConstructor
public class HillelUserDetailService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> byEmail = userRepo.findByEmail(username);

            User appUser = byEmail.orElseThrow(() -> new UsernameNotFoundException(username));

            return new HillelUserDetails(appUser);
        } catch (ExpenseTrackerPersistingException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
