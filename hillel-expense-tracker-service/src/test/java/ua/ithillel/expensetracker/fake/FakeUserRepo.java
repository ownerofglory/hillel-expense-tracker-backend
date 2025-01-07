package ua.ithillel.expensetracker.fake;

import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FakeUserRepo implements UserRepo {
    private Set<User> users = new HashSet<>() {{
        add(new User());
        add(new User());
    }};

    @Override
    public Optional<User> find(Long id) throws ExpenseTrackerPersistingException {
        return Optional.ofNullable(new User());
    }

    @Override
    public Optional<User> findByEmail(String email) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }

    @Override
    public Optional<User> save(User user) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(User user) throws ExpenseTrackerPersistingException {
        return Optional.empty();
    }
}
