package ua.ithillel.expensetracker.client.exception;

public class GptChatCompletionException extends RuntimeException {
    public GptChatCompletionException(String message) {
        super(message);
    }
}
