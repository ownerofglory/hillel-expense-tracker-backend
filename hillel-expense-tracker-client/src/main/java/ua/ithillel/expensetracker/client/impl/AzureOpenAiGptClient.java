package ua.ithillel.expensetracker.client.impl;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import com.azure.core.util.BinaryData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import lombok.RequiredArgsConstructor;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.client.exception.GptChatCompletionException;
import ua.ithillel.expensetracker.client.model.GptMessage;
import ua.ithillel.expensetracker.client.model.GptMessageContent;
import ua.ithillel.expensetracker.client.model.GptResponse;
import ua.ithillel.expensetracker.client.schema.GptJsonSchema;
import ua.ithillel.expensetracker.client.schema.GptResponseSchema;

import java.util.List;

@RequiredArgsConstructor
public class AzureOpenAiGptClient implements GPTClient {
    private static final String DEPLOYMENT_MODEL = "gpt-4o-mini";

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    @Override
    public GptResponse<String> getChatCompletion(List<GptMessage> messages) {
        List<ChatRequestMessage> chatRequestMessages = convertMessages(messages);
        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatRequestMessages);

        ChatCompletions chatCompletions = openAIClient.getChatCompletions(DEPLOYMENT_MODEL, chatCompletionsOptions);
        ChatChoice chatChoice = chatCompletions.getChoices().getFirst();
        ChatResponseMessage message = chatChoice.getMessage();
        String content = message.getContent();

        GptResponse<String> objectGptResponse = new GptResponse<>();
        objectGptResponse.setContent(content);
        return objectGptResponse;
    }

    @Override
    public <T> GptResponse<T> getChatCompletionWithResponseType(List<GptMessage> messages, Class<T> clazz) {
        try {
            JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);
            JsonSchema jsonSchema = jsonSchemaGenerator.generateSchema(clazz);
            GptJsonSchema gptJsonSchema = new GptJsonSchema((ObjectSchema) jsonSchema);
            GptResponseSchema gptResponseSchema = new GptResponseSchema(gptJsonSchema, clazz.getSimpleName(), clazz.getSimpleName(), true);


            List<ChatRequestMessage> chatRequestMessages = convertMessages(messages);
            ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatRequestMessages);

            String jsonRespSchema = objectMapper.writeValueAsString(gptResponseSchema.getSchema());
            ChatCompletionsJsonSchemaResponseFormat chatCompletionsJsonSchemaResponseFormat = new ChatCompletionsJsonSchemaResponseFormat(
                    new ChatCompletionsJsonSchemaResponseFormatJsonSchema(gptResponseSchema.getName())
                            .setStrict(true)
                            .setDescription(gptResponseSchema.getDescription())
                            .setSchema(
                                    BinaryData.fromString(jsonRespSchema)
                            )
            );
            chatCompletionsOptions.setResponseFormat(chatCompletionsJsonSchemaResponseFormat);

            ChatCompletions chatCompletions = openAIClient.getChatCompletions(DEPLOYMENT_MODEL, chatCompletionsOptions);
            ChatChoice chatChoice = chatCompletions.getChoices().getFirst();
            ChatResponseMessage message = chatChoice.getMessage();
            String content = message.getContent();

            T responseValue = objectMapper.readValue(content, clazz);

            GptResponse<T> objectGptResponse = new GptResponse<>();
            objectGptResponse.setContent(responseValue);

            return objectGptResponse;
        } catch (JsonProcessingException e) {
            throw new GptChatCompletionException("Failed to get chat completion");
        }
    }

    private List<ChatRequestMessage> convertMessages(List<GptMessage> messages) {
        return messages.stream().map(m -> {
            GptMessageContent content = m.getContent();
            ChatMessageContentItem contentItem =  switch (content.getType()) {
                case "image_url" -> new ChatMessageImageContentItem(new ChatMessageImageUrl(content.getImageUrl()));
                case "text" -> new ChatMessageTextContentItem(content.getTextContent());
                default -> new ChatMessageTextContentItem(content.getTextContent());
            };

            return switch (m.getRole()) {
                case "user" -> new ChatRequestUserMessage(List.of(contentItem));
                case "assistant" -> new ChatRequestAssistantMessage(List.of(contentItem));
                default -> new ChatRequestUserMessage(List.of(contentItem));
            };
        }).toList();
    }
}
