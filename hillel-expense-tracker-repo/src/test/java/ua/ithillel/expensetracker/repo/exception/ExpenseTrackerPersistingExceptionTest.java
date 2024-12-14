package ua.ithillel.expensetracker.repo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExpenseTrackerPersistingExceptionTest {
    @Test
    void testExceptionWithEntityAndMessage() {
        Object entity = new Object();
        String message = "An error occurred";

        ExpenseTrackerPersistingException exception = new ExpenseTrackerPersistingException(entity, message, null);

        assertEquals(message, exception.getMessage());
        assertEquals(entity, exception.getEntity());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionWithCause() {
        Object entity = new Object();
        String message = "An error occurred";
        Throwable cause = new RuntimeException("Underlying exception");

        ExpenseTrackerPersistingException exception = new ExpenseTrackerPersistingException(entity, message, cause);

        assertEquals(cause, exception.getCause());
    }
}
