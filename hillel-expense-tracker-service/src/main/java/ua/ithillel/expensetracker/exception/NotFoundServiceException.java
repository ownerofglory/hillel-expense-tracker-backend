package ua.ithillel.expensetracker.exception;

public class NotFoundServiceException extends ServiceException {
    public NotFoundServiceException(String message) {
        super(message);
    }
}
