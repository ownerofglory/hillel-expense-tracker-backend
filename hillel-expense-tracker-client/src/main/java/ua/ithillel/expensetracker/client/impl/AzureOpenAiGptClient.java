package ua.ithillel.expensetracker.client.impl;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import com.azure.core.util.BinaryData;
import com.azure.core.util.IterableStream;
import com.azure.json.JsonOptions;
import com.azure.json.JsonReader;
import com.azure.json.implementation.DefaultJsonReader;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.client.schema.GptJsonSchema;
import ua.ithillel.expensetracker.client.schema.GptResponseSchema;
import ua.ithillel.expensetracker.client.tool.AgentToolType;
import ua.ithillel.expensetracker.client.tool.GptTool;
import ua.ithillel.expensetracker.client.tool.GptToolChoice;
import ua.ithillel.expensetracker.exception.GptChatCompletionException;
import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptMessageContent;
import ua.ithillel.expensetracker.model.GptResponse;
import ua.ithillel.expensetracker.model.GptToolResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class AzureOpenAiGptClient implements GPTClient {
    private static final String DEPLOYMENT_MODEL = "gpt-4o-mini";

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    @Override
    public GptResponse<String> getChatCompletion(List<GptMessage> messages) {
        String model = System.getenv("OPENAI_MODEL");
        if (model == null || model.isEmpty()) {
            model = DEPLOYMENT_MODEL;
        }

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
    public Stream<GptResponse<String>> getChatCompletionStream(List<GptMessage> messages) {
        String model = System.getenv("OPENAI_MODEL");
        if (model == null || model.isEmpty()) {
            model = DEPLOYMENT_MODEL;
        }

        List<ChatRequestMessage> chatRequestMessages = convertMessages(messages);
        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatRequestMessages);

        IterableStream<ChatCompletions> chatCompletionsStream = openAIClient.getChatCompletionsStream(DEPLOYMENT_MODEL, chatCompletionsOptions);

        return chatCompletionsStream.stream()
                .map(ChatCompletions::getChoices)
                .map(List::getFirst)
                .map(ChatChoice::getDelta)
                .map(ChatResponseMessage::getContent)
                .map(m -> {
                    GptResponse<String> stringGptResponse = new GptResponse<>();
                    stringGptResponse.setContent(m);

                    return stringGptResponse;
                });
    }

    @Override
    public <T> GptResponse<T> getChatCompletionWithResponseType(List<GptMessage> messages, Class<T> clazz) {
        try {
            String model = System.getenv("OPENAI_MODEL");
            if (model == null || model.isEmpty()) {
                model = DEPLOYMENT_MODEL;
            }

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

    @Override
    public <Tfunc extends AgentToolType> GptToolResponse getChatCompletionWithTools(List<GptMessage> messages, List<GptTool<? extends AgentToolType>> gptTools) {
        try {
            String model = System.getenv("OPENAI_MODEL");
            if (model == null || model.isEmpty()) {
                model = DEPLOYMENT_MODEL;
            }


            List<ChatCompletionsToolDefinition> toolDefinitions = convertTools(gptTools);
            List<ChatRequestMessage> chatRequestMessages = convertMessages(messages);

            ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatRequestMessages);
            chatCompletionsOptions.setTools(toolDefinitions);

            ChatCompletions chatCompletions = openAIClient.getChatCompletions(DEPLOYMENT_MODEL, chatCompletionsOptions);
            ChatChoice chatChoice = chatCompletions.getChoices().getFirst();

            if (chatChoice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
                ChatCompletionsFunctionToolCall toolCall = (ChatCompletionsFunctionToolCall) chatChoice.getMessage().getToolCalls().getFirst();
                String functionName = toolCall.getFunction().getName();
                String functionArguments = toolCall.getFunction().getArguments();
                String functionId = toolCall.getId();

                FunctionWrapper functionWrapper = new FunctionWrapper(functionName, functionArguments);
                ToolWrapper toolWrapper = new ToolWrapper(functionId, functionWrapper);
                String toolRaw = objectMapper.writeValueAsString(toolWrapper);


                GptToolResponse gptToolResponse = new GptToolResponse();
                GptToolChoice gptToolChoice = new GptToolChoice(functionId, functionName, functionArguments, toolRaw);
                gptToolResponse.setTool(gptToolChoice);

                return gptToolResponse;
            }

            String content = chatChoice.getMessage().getContent();
            GptToolResponse gptToolResponse = new GptToolResponse();
            gptToolResponse.setContent(content);
            return gptToolResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <Tfunc extends AgentToolType> Stream<GptToolResponse> getChatCompletionWithToolsStream(List<GptMessage> messages, List<GptTool<? extends AgentToolType>> gptTools) {
        String model = System.getenv("OPENAI_MODEL");
        if (model == null || model.isEmpty()) {
            model = DEPLOYMENT_MODEL;
        }

        List<ChatCompletionsToolDefinition> toolDefinitions = convertTools(gptTools);
        List<ChatRequestMessage> chatRequestMessages = convertMessages(messages);

        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatRequestMessages);
        chatCompletionsOptions.setTools(toolDefinitions);

        IterableStream<ChatCompletions> chatCompletionsStream = openAIClient.getChatCompletionsStream(DEPLOYMENT_MODEL, chatCompletionsOptions);

        StringBuilder accumulatedFunctionName = new StringBuilder();
        StringBuilder accumulatedArguments = new StringBuilder();
        StringBuilder accumulatedId = new StringBuilder();
        final AtomicBoolean functionCallInProgress = new AtomicBoolean(false);

        return chatCompletionsStream.stream().map(chatCompletions -> {
            ChatChoice chatChoice = chatCompletions.getChoices().getFirst();
            ChatResponseMessage delta = chatChoice.getDelta();

            // intermediate tool processing
            // if tool (function) call detected
            if (delta.getToolCalls() != null && !delta.getToolCalls().isEmpty()) {
                functionCallInProgress.set(true);

                // Append to function name if present
                ChatCompletionsFunctionToolCall toolCall = (ChatCompletionsFunctionToolCall) delta.getToolCalls().getFirst();
                if (toolCall.getFunction().getName() != null) {
                    accumulatedFunctionName.append(toolCall.getFunction().getName());
                }
                if (toolCall.getFunction().getArguments() != null) {
                    accumulatedArguments.append(toolCall.getFunction().getArguments());
                }
                if (toolCall.getId() != null) {
                    accumulatedId.append(toolCall.getId());
                }

                GptToolResponse gptToolResponse = new GptToolResponse();
                gptToolResponse.setIntermediate(true);
                return gptToolResponse;
            }

            // terminal tool processing
            if (functionCallInProgress.get() && !accumulatedFunctionName.isEmpty() && !accumulatedArguments.isEmpty()) {
                try {
                    functionCallInProgress.set(false);

                    String functionName = accumulatedFunctionName.toString();
                    String functionArguments = accumulatedArguments.toString();
                    String functionId = accumulatedId.toString();

                    FunctionWrapper functionWrapper = new FunctionWrapper(functionName, functionArguments);
                    ToolWrapper toolWrapper = new ToolWrapper(functionId, functionWrapper);
                    String toolRaw = objectMapper.writeValueAsString(toolWrapper);


                    GptToolResponse functionResponse = new GptToolResponse();
                    functionResponse.setTool(new GptToolChoice(functionId ,functionName, functionArguments, toolRaw));

                    accumulatedFunctionName.setLength(0);
                    accumulatedArguments.setLength(0);

                    return functionResponse;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            // regular message chunk
            GptToolResponse gptToolResponse = new GptToolResponse();
            gptToolResponse.setContent(delta.getContent());
            return gptToolResponse;
        }).filter(r -> !r.isIntermediate()); // avoid intermediate chunks
    }

    private List<ChatCompletionsToolDefinition> convertTools(List<GptTool<? extends AgentToolType>> gptTools) {
        return gptTools.stream().map(this::convertTool).toList();
    }

    private <T> ChatCompletionsToolDefinition convertTool(GptTool<T> gptTool) {
        ChatCompletionsFunctionToolDefinitionFunction functionDefinition = new ChatCompletionsFunctionToolDefinitionFunction(gptTool.getName());
        functionDefinition.setDescription(gptTool.getDescription());
        functionDefinition.setParameters(BinaryData.fromObject(gptTool.getParameters()));

        return (ChatCompletionsToolDefinition) new ChatCompletionsFunctionToolDefinition(functionDefinition);
    }

    private List<ChatRequestMessage> convertMessages(List<GptMessage> messages) {
        return messages.stream().map(m -> {
            try {
                return convertMessage(m);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private ChatRequestMessage convertMessage(GptMessage m) throws IOException {
        GptMessageContent content = m.getContent();
        ChatMessageContentItem contentItem =  switch (content.getType()) {
            case GptMessageContent.CONTENT_TYPE_IMAGE_URL -> new ChatMessageImageContentItem(new ChatMessageImageUrl(content.getImageUrl()));
            case GptMessageContent.CONTENT_TYPE_TEXT -> new ChatMessageTextContentItem(content.getTextContent());
            case GptMessageContent.CONTENT_TYPE_TOOL_CALL_RESULT -> new ChatMessageTextContentItem(content.getToolCallResult());
            case GptMessageContent.CONTENT_TYPE_TOOL_CALL -> null;
            default -> new ChatMessageTextContentItem(content.getTextContent());
        };

        ChatRequestAssistantMessage assistantToolMessage = new ChatRequestAssistantMessage("");

        final List<ChatMessageContentItem> contentItems = new ArrayList<>();
        contentItems.add(contentItem);

        return switch (m.getRole()) {
            case GptMessage.ROLE_USER -> new ChatRequestUserMessage(contentItems);
            case GptMessage.ROLE_ASSISTANT -> {
                if (contentItem == null) {
                    JsonReader jsonReader = DefaultJsonReader.fromString(m.getContent().getToolRaw(), new JsonOptions());
                    ChatCompletionsToolCall chatCompletionsToolCall = ChatCompletionsToolCall.fromJson(jsonReader);
                    assistantToolMessage.setToolCalls(List.of(chatCompletionsToolCall));
                    yield assistantToolMessage;
                }
                yield new ChatRequestAssistantMessage(contentItems);
            }
            case GptMessage.ROLE_TOOL -> new ChatRequestToolMessage(contentItems, content.getToolCallId());
            case GptMessage.ROLE_SYSTEM -> new ChatRequestSystemMessage(contentItems);
            default -> new ChatRequestUserMessage(contentItems);
        };
    }

    @Data
    private static class ToolWrapper {
        @JsonProperty("id")
        public String id;
        @JsonProperty("type")
        public String type = "function";
        @JsonProperty("function")
        private FunctionWrapper function;

        public ToolWrapper(String id, FunctionWrapper function) {
            this.id = id;
            this.function = function;
        }
    }

    @Data
    private static class FunctionWrapper {
        @JsonProperty("name")
        private String name;
        @JsonProperty("arguments")
        private String arguments;

        public FunctionWrapper(String name, String arguments) {
            this.name = name;
            this.arguments = arguments;
        }
    }
}
