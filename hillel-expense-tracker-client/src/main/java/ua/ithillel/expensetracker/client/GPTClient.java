package ua.ithillel.expensetracker.client;

import ua.ithillel.expensetracker.client.model.GptMessage;
import ua.ithillel.expensetracker.client.model.GptResponse;

import java.util.List;

public interface GPTClient {
    /**
     *
     * @param messages list of GptMessage objects
     * @return text response wrapped into GptResponse object
     */
    GptResponse<String> getChatCompletion(List<GptMessage> messages);

    /**
     *
     * @param messages list of GptMessage objects
     * @param clazz class metadata that will be use as JSON-schema for structured response
     * @return object response of a given data type specified by 'clazz' param
     * @param <T> response object type
     */
    <T> GptResponse<T> getChatCompletionWithResponseType(List<GptMessage> messages, Class<T> clazz);
}
