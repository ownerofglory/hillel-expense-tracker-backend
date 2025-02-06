package ua.ithillel.expensetracker.client.impl;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.util.BinaryData;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.client.tool.AgentToolType;
import ua.ithillel.expensetracker.client.tool.GptTool;
import ua.ithillel.expensetracker.client.tool.GptToolChoice;
import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptMessageContent;
import ua.ithillel.expensetracker.model.GptResponse;
import ua.ithillel.expensetracker.model.GptToolResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AzureOpenAiGptClientTest {
    private WireMockServer wireMockServer;
    private AzureOpenAiGptClient azureOpenAiGptClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        OpenAIClient openAIClient = new OpenAIClientBuilder().endpoint("http://localhost:8080/chat").buildClient();
        ObjectMapper objectMapper = new ObjectMapper();
        azureOpenAiGptClient = new AzureOpenAiGptClient(openAIClient, objectMapper);
    }

    @Test
    void getChatCompletionStreamTest_returnsMessages() {
        String mockResponseBody = getMockText("mock-gpt-stream-response.txt");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        okForContentType("text/event-stream", mockResponseBody)
                )
        );

        GptMessageContent gptMessageContent1 = new GptMessageContent();
        gptMessageContent1.setType("text");
        gptMessageContent1.setTextContent("Test prompt");
        GptMessage gptMessage2 = new GptMessage("user", gptMessageContent1);

        List<GptMessage> messages = List.of(
                gptMessage2
        );

        Stream<GptResponse<String>> chatCompletionWithToolsStream = azureOpenAiGptClient.getChatCompletionStream(messages);
        assertNotNull(chatCompletionWithToolsStream);
        chatCompletionWithToolsStream.forEach(Assertions::assertNotNull);
    }

    @Test
    void getChatCompletionWithToolsStreamTest_returnsMessages() {
        String mockResponseBody = getMockText("mock-gpt-stream-response.txt");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        okForContentType("text/event-stream", mockResponseBody)
                )
        );

        GptMessageContent gptMessageContent1 = new GptMessageContent();
        gptMessageContent1.setType("text");
        gptMessageContent1.setTextContent("Test prompt");
        GptMessage gptMessage2 = new GptMessage("user", gptMessageContent1);

        GptMessageContent gptMessageToolContent = new GptMessageContent();
        gptMessageToolContent.setType(GptMessageContent.CONTENT_TYPE_TOOL_CALL_RESULT);
        gptMessageToolContent.setToolCallId("testId");
        gptMessageToolContent.setToolCallResult("success");
        GptMessage gptToolResultMessage = new GptMessage(GptMessage.ROLE_TOOL, gptMessageToolContent);

        List<GptMessage> messages = List.of(
                gptMessage2, gptToolResultMessage
        );

        GptTool<FuncParams> tool = new GptTool<>();
        tool.setName("StoreUser");
        tool.setDescription("Stores user by given name and age and returns saved user");
        tool.setParameters(new FuncParams());
        List<GptTool<? extends AgentToolType>> tools = List.of(
                tool
        );

        Stream<GptToolResponse> chatCompletionWithToolsStream = azureOpenAiGptClient.getChatCompletionWithToolsStream(messages, tools);
        assertNotNull(chatCompletionWithToolsStream);
        chatCompletionWithToolsStream.forEach(Assertions::assertNotNull);
    }

    @Test
    void getChatCompletionWithToolsStreamTest_returnsToolCall() {
        String mockResponseBody = getMockText("mock-gpt-tool-stream-response.txt");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        okForContentType("text/event-stream", mockResponseBody)
                )
        );

        GptMessageContent gptMessageContent1 = new GptMessageContent();
        gptMessageContent1.setType("text");
        gptMessageContent1.setTextContent("Get current temperature for a given location.");
        GptMessage gptMessage2 = new GptMessage("user", gptMessageContent1);

        List<GptMessage> messages = List.of(
                gptMessage2
        );

        GptTool<FuncParams> tool = new GptTool<>();
        tool.setName("StoreUser");
        tool.setDescription("Stores user by given name and age and returns saved user");
        tool.setParameters(new FuncParams());
        List<GptTool<? extends AgentToolType>> tools = List.of(
                tool
        );

        Stream<GptToolResponse> chatCompletionWithToolsStream = azureOpenAiGptClient.getChatCompletionWithToolsStream(messages, tools);
        assertNotNull(chatCompletionWithToolsStream);
        GptToolResponse gptToolResponse = chatCompletionWithToolsStream.findFirst().orElse(null);
        assertNotNull(gptToolResponse);
        assertNotNull(gptToolResponse.getTool());
        assertNotNull(gptToolResponse.getTool().getToolName());
    }

    @Test
    void getChatCompletionWithToolsTest_returnsToolCall() {
        String mockResponseBody = getMockJson("mock-gpt-tool-response.json");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        aResponse().withBody(mockResponseBody)
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                )
        );

        GptMessageContent gptMessageContent1 = new GptMessageContent();
        gptMessageContent1.setType("text");
        gptMessageContent1.setTextContent("Get current temperature for a given location.");
        GptMessage gptMessage2 = new GptMessage("user", gptMessageContent1);

        List<GptMessage> messages = List.of(
                gptMessage2
        );

        GptTool<FuncParams> tool = new GptTool<>();
        tool.setName("StoreUser");
        tool.setDescription("Stores user by given name and age and returns saved user");
        tool.setParameters(new FuncParams());
        List<GptTool<? extends AgentToolType>> tools = List.of(
            tool
        );

        GptToolResponse chatCompletionWithTools = azureOpenAiGptClient.getChatCompletionWithTools(messages, tools);
        assertNotNull(chatCompletionWithTools);
        assertNotNull(chatCompletionWithTools.getTool());
        GptToolChoice chosenTool = chatCompletionWithTools.getTool();
        FuncArgs funcArgs = BinaryData.fromString(chosenTool.getArgs()).toObject(FuncArgs.class);
        assertNotNull(funcArgs);
        assertNotNull(funcArgs.location);
    }

    @Test
    void getChatCompletionWithToolsTest_returnsText() {
        String mockResponseBody = getMockJson("mock-gpt-response.json");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        aResponse().withBody(mockResponseBody)
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                )
        );

        GptMessageContent gptMessageContent1 = new GptMessageContent();
        gptMessageContent1.setType("text");
        gptMessageContent1.setTextContent("Get current temperature for a given location.");
        GptMessage gptMessage2 = new GptMessage("user", gptMessageContent1);

        List<GptMessage> messages = List.of(
                gptMessage2
        );

        GptTool<FuncParams> tool = new GptTool<>();
        tool.setName("StoreUser");
        tool.setDescription("Test prompt");
        tool.setParameters(new FuncParams());
        List<GptTool<? extends AgentToolType>> tools = List.of(
                tool
        );

        GptToolResponse chatCompletionWithTools = azureOpenAiGptClient.getChatCompletionWithTools(messages, tools);
        assertNotNull(chatCompletionWithTools);
        assertNull(chatCompletionWithTools.getTool());
        assertNotNull(chatCompletionWithTools.getContent());
    }

    @Test
    void getChatCompletionTest_returnsText() {
        String mockResponseBody = getMockJson("mock-gpt-response.json");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        aResponse().withBody(mockResponseBody)
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                )
        );

        String imageUrl = getMockJson("image.txt");

        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType("image_url");
        gptMessageContent.setImageUrl(imageUrl);
        GptMessage gptMessage1 = new GptMessage("user", gptMessageContent);

        GptMessageContent gptMessageContent1 = new GptMessageContent();
        gptMessageContent1.setType("text");
        gptMessageContent1.setTextContent("Please tell me that is the country on picture and its population");
        GptMessage gptMessage2 = new GptMessage("user", gptMessageContent1);


        List<GptMessage> messages = List.of(
                gptMessage1, gptMessage2
        );

        GptResponse<String> chatCompletionWithResponseType = azureOpenAiGptClient.getChatCompletion(messages);

        assertNotNull(chatCompletionWithResponseType);
    }

    @Test
    void getChatCompletionWithResponseTypeTest_returnsResponse() {
        String mockResponseBody = getMockJson("mock-gpt-structured-response.json");

        configureFor("localhost", 8080);
        stubFor(
          post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                  aResponse().withBody(mockResponseBody)
                          .withStatus(200)
                          .withHeader("Content-Type", "application/json")
          )
        );



        String imageUrl = getMockJson("image.txt");

        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType("image_url");
        gptMessageContent.setImageUrl(imageUrl);
        GptMessage gptMessage1 = new GptMessage("user", gptMessageContent);

        GptMessageContent gptMessageContent1 = new GptMessageContent();
        gptMessageContent1.setType("text");
        gptMessageContent1.setTextContent("Please tell me that is the country on picture and its population");
        GptMessage gptMessage2 = new GptMessage("user", gptMessageContent1);


        List<GptMessage> messages = List.of(
                gptMessage1, gptMessage2
        );

        GptResponse<CountryPopulation> chatCompletionWithResponseType = azureOpenAiGptClient.getChatCompletionWithResponseType(messages, CountryPopulation.class);

        assertNotNull(chatCompletionWithResponseType);
        assertNotNull(chatCompletionWithResponseType.getContent().country);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Test class that represents the response structure")
    static class CountryPopulation {
        @JsonProperty(required = true)
        @JsonPropertyDescription("Name of the country")
        private String country;
        @JsonProperty(required = true)
        @JsonPropertyDescription("Population of the country")
        private Integer population;
    }

    private String getMockJson(String fileName) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines()
                .reduce((line, acc) -> line + acc)
                .get();
    }

    private String getMockText(String fileName) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines()
                .reduce((line, acc) -> line + acc + "\n")
                .get();
    }

    private static class FuncArgs {
        @JsonProperty("location")
        private String location;
    }

    private static class FuncParams implements AgentToolType {
        @JsonProperty(value = "type")
        private String type = "object";

        @JsonProperty(value = "properties")
        private TestFuncParams properties = new TestFuncParams();

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public Object getParams() {
            return null;
        }
    }

    private static class TestFuncParams {
        @JsonProperty("location")
        private StringField location = new StringField("City and country e.g. Bogotá, Colombia");
    }

    private static class StringField {
        @JsonProperty(value = "type")
        private final String type = "string";

        @JsonProperty(value = "description")
        private String description;

        @JsonCreator
        StringField(@JsonProperty(value = "description") String description) {
            this.description = description;
        }
    }
}
