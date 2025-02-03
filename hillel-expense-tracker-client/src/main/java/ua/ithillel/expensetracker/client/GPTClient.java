package ua.ithillel.expensetracker.client;

import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptResponse;
import ua.ithillel.expensetracker.client.tool.GptTool;
import ua.ithillel.expensetracker.model.GptToolResponse;

import java.util.List;
import java.util.stream.Stream;

public interface GPTClient {
    /**
     * Creates a chat completion text
     *
     * @param messages list of GptMessage objects
     * @return text response wrapped into GptResponse object
     */
    GptResponse<String> getChatCompletion(List<GptMessage> messages);

    /**
     * Creates a chat completion text stream
     *
     * @param messages list of GptMessage objects
     * @return stream of GptResponse objects
     */
    Stream<GptResponse<String>> getChatCompletionStream(List<GptMessage> messages);

    /**
     * Creates a chat completion object of a given type
     *
     * @param messages list of GptMessage objects
     * @param clazz    class metadata that will be used as JSON-schema for structured response
     * @param <T>      response object type
     * @return object response of a given data type specified by 'clazz' param
     */
    <T> GptResponse<T> getChatCompletionWithResponseType(List<GptMessage> messages, Class<T> clazz);

    /**
     * Creates a chat completion with a possible tool call
     *
     * @param messages list of GptMessage objects
     * @param gptTools list of GptTools that can be called
     * @return GptToolResponse that contains either chat completion text or tool call
     * @param <Tfunc> tool object type
     */
    <Tfunc> GptToolResponse getChatCompletionWithTools(List<GptMessage> messages, List<GptTool<Tfunc>> gptTools);

    /**
     * Creates a chat completion with a possible tool call.
     * Returns a stream of chunks if result of the completion is text.
     * If result is a tool call returns one single chunk with tool information
     *
     * @param messages list of GptMessage objects
     * @param gptTools list of GptTools that can be called
     * @return Stream of GptToolResponse chunks that contains either chat completion text or tool call
     * @param <Tfunc> tool object type
     */
    <Tfunc> Stream<GptToolResponse> getChatCompletionWithToolsStream(List<GptMessage> messages, List<GptTool<Tfunc>> gptTools);
}
