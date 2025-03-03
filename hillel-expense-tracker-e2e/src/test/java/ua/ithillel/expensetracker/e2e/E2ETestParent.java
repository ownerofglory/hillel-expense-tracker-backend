package ua.ithillel.expensetracker.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class E2ETestParent {
    protected WireMockServer wireMockServer;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mvc;

    @BeforeEach
    public void setupMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    public void setupOpenAI() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        String mockResponseBody = getMockText("mock-gpt-stream-response.txt");

        configureFor("localhost", 8080);
        stubFor(
                post(urlEqualTo("/chat/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-10-01-preview")).willReturn(
                        okForContentType("text/event-stream", mockResponseBody)
                )
        );
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    private String getMockJson(String fileName) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines()
                .reduce((line, acc) -> line + acc)
                .get();
    }

    protected String getMockText(String fileName) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines()
                .reduce((line, acc) -> line + acc + "\n")
                .get();
    }

}
