package ua.ithillel.expensetracker.exception;

public class ServiceErrorException extends ServiceException {
    public ServiceErrorException(String message) {
        super(message);
    }
}
