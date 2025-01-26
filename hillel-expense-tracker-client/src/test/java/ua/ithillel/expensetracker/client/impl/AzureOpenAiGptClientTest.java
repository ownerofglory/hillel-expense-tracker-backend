package ua.ithillel.expensetracker.client.impl;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.KeyCredential;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.client.model.GptMessage;
import ua.ithillel.expensetracker.client.model.GptMessageContent;
import ua.ithillel.expensetracker.client.model.GptResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void getChatCompletionTest_returnsText() {
        String mockResponseBody = getMockText("mock-gpt-response.json");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-08-01-preview")).willReturn(
                        aResponse().withBody(mockResponseBody)
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                )
        );

        String imageUrl = getMockText("image.txt");

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
        String mockResponseBody = getMockText("mock-gpt-structured-response.json");

        configureFor("localhost", 8080);
        stubFor(
          post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-08-01-preview")).willReturn(
                  aResponse().withBody(mockResponseBody)
                          .withStatus(200)
                          .withHeader("Content-Type", "application/json")
          )
        );



        String imageUrl = getMockText("image.txt");

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

    private String getMockText(String fileName) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines()
                .reduce((line, acc) -> line + acc)
                .get();
    }
}
