package ua.ithillel.expensetracker.e2e;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptMessageContent;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SmartAgentE2ETest extends E2ETestParent {
    @Test
    @WithUserDetails("john.doe@example.com")
    void getChatCompletionTest_Ok() throws Exception {
        String mockResponseBody = getMockText("mock-gpt-stream-response.txt");

        configureFor("localhost", 8080);
        stubFor(
                WireMock.post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        okForContentType("text/event-stream", mockResponseBody)
                )
        );

        GptMessage gptMessage = new GptMessage();
        gptMessage.setRole(GptMessage.ROLE_USER);
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setTextContent("Test message");
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
        gptMessage.setContent(gptMessageContent);
        List<GptMessage> messages = List.of(gptMessage);

        String body = objectMapper.writeValueAsString(messages);

        mvc.perform(
                        post("/v1/agent?userId=1")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.TEXT_EVENT_STREAM)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void getChatCompletionTest_anonymousUserUnauthorized() throws Exception {
        String mockResponseBody = getMockText("mock-gpt-stream-response.txt");

        configureFor("localhost", 8080);
        stubFor(
                WireMock.post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        okForContentType("text/event-stream", mockResponseBody)
                )
        );

        GptMessage gptMessage = new GptMessage();
        gptMessage.setRole(GptMessage.ROLE_USER);
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setTextContent("Test message");
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
        gptMessage.setContent(gptMessageContent);
        List<GptMessage> messages = List.of(gptMessage);

        String body = objectMapper.writeValueAsString(messages);

        mvc.perform(
                        post("/v1/agent?userId=1")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getChatCompletionTest_noUserUnauthorized() throws Exception {
        String mockResponseBody = getMockText("mock-gpt-stream-response.txt");

        configureFor("localhost", 8080);
        stubFor(
                WireMock.post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        okForContentType("text/event-stream", mockResponseBody)
                )
        );

        GptMessage gptMessage = new GptMessage();
        gptMessage.setRole(GptMessage.ROLE_USER);
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setTextContent("Test message");
        gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
        gptMessage.setContent(gptMessageContent);
        List<GptMessage> messages = List.of(gptMessage);

        String body = objectMapper.writeValueAsString(messages);

        mvc.perform(
                        post("/v1/agent?userId=1")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }
}
