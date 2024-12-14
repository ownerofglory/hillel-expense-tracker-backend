package ua.ithillel.expensetracker.repo.exception;

/*
    Custom exception for persisting errors in the Expense Tracker application.
 */
public class ExpenseTrackerPersistingException extends Exception {
    private final Object entity;

    public ExpenseTrackerPersistingException(final Object entity, String message, Throwable cause) {
        super(message, cause);
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }
}