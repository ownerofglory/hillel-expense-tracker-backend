package ua.ithillel.expensetracker.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ua.ithillel.expensetracker.util.JwtUtil;

@TestConfiguration
public class SecurityTestConfig {
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
