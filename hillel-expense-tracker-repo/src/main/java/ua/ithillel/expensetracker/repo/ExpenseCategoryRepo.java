package ua.ithillel.expensetracker.repo;

import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface for managing Expense Category repository operations.
 */
public interface ExpenseCategoryRepo {

    /**
     * Finds an expense category by ID
     *
     * @param id the ID of the expense category
     * @return an Optional containing the found expense category or empty if not found
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    Optional<ExpenseCategory> find(Long id) throws ExpenseTrackerPersistingException;

    /**
     * Saves an expense category object
     *
     * @param expenseCategory the expense category to save
     * @return an Optional containing the saved expense category
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    Optional<ExpenseCategory> save(ExpenseCategory expenseCategory) throws ExpenseTrackerPersistingException;

    /**
     * Finds expense categories by a user
     *
     * @param user the user whose expense categories are to be retrieved
     * @return a list of expense categories belonging to the user
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    List<ExpenseCategory> findByUser(User user) throws ExpenseTrackerPersistingException;

    /**
     * Deletes an expense category object
     *
     * @param expenseCategory the expense category to delete
     * @return an Optional containing the deleted expense category
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    Optional<ExpenseCategory> delete(ExpenseCategory expenseCategory) throws ExpenseTrackerPersistingException;
}