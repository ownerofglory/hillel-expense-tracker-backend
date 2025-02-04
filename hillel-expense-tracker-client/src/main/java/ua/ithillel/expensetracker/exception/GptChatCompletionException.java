package ua.ithillel.expensetracker.exception;

public class GptChatCompletionException extends RuntimeException {
    public GptChatCompletionException(String message) {
        super(message);
    }
}
