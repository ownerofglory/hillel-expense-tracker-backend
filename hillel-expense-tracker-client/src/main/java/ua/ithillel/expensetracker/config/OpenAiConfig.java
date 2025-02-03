package ua.ithillel.expensetracker.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.KeyCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {
    @Bean
    public OpenAIClient getOpenAIClient() {
        String openaiKey = System.getenv("OPENAI_KEY");
        String openaiEndpoint = System.getenv("OPENAI_ENDPOINT");
        return new OpenAIClientBuilder()
                .endpoint(openaiEndpoint)
                .credential(new KeyCredential(openaiKey))
                .buildClient();
    }
}
