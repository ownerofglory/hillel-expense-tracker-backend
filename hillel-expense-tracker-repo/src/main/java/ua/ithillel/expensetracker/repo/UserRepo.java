package ua.ithillel.expensetracker.repo;

import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;

import java.util.Optional;

public interface UserRepo {

    Optional<User> find(Long id) throws ExpenseTrackerPersistingException;

    Optional<User> findByEmail(String email) throws ExpenseTrackerPersistingException;

    Optional<User> save(User user) throws ExpenseTrackerPersistingException;

    Optional<User> delete(User user) throws ExpenseTrackerPersistingException;
}
