package ua.ithillel.expensetracker.exception;

import lombok.RequiredArgsConstructor;

public class DaoException extends Exception {
    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
