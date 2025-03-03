package ua.ithillel.expensetracker.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class OpenAiTestConfig {
    public static final String TEST_ENDPOINT = "http://localhost:8080/chat";

    @Bean
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .endpoint(TEST_ENDPOINT)
                .buildClient();
    }
}
