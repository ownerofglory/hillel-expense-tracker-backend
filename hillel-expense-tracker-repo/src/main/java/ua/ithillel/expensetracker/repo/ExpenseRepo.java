package ua.ithillel.expensetracker.repo;

import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepo {

    /**
     * Finds an expense by ID
     *
     * @param id the ID of the expense
     * @return an Optional containing the found expense ir empty if not found
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    Optional<Expense> find(Long id) throws ExpenseTrackerPersistingException;

    /**
     * Saves an expense object
     *
     * @param expense the expense to save
     * @return an Optional containing the saved expense
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    Optional<Expense> save(Expense expense) throws ExpenseTrackerPersistingException;

    /**
     * Finds an expense by a user
     *
     * @param user the user whose expenses are to be retrieved
     * @return a list of expense belonging to the user
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    List<Expense> findByUser(User user) throws ExpenseTrackerPersistingException;

    PaginationResponse<List<Expense>> findByUser(User user, PaginationReq paginationReq) throws ExpenseTrackerPersistingException;

    List<Expense> findByUserBetweenDates(User user, LocalDateTime from, LocalDateTime to) throws ExpenseTrackerPersistingException;

    /**
     * Finds an expense by a category
     *
     * @param category the category of the expense
     * @return a list of expenses under the specified category
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    List<Expense> findByCategory(ExpenseCategory category) throws ExpenseTrackerPersistingException;

    /**
     * Deletes an expense object
     *
     * @param expense the expense to delete
     * @return an Optional containing the delete expense
     * @throws ExpenseTrackerPersistingException if any persisting error occurs
     */
    Optional<Expense> delete(Expense expense) throws ExpenseTrackerPersistingException;
}
