package ua.ithillel.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public ExternalCategoryClientFactory getExternalCategoryClientFactory() {
        return new ExternalCategoryClientFactory();
    }
}
