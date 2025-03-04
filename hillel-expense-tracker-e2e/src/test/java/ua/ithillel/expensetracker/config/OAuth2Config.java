package ua.ithillel.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import ua.ithillel.expensetracker.auth.GoogleAuthErrorHandler;
import ua.ithillel.expensetracker.auth.GoogleAuthSuccessHandler;
import ua.ithillel.expensetracker.client.GoogleAccessResponseClient;
import ua.ithillel.expensetracker.service.GoogleOAuth2UserService;
import ua.ithillel.expensetracker.service.GoogleOidcUserService;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class OAuth2Config {
    @Bean
    public GoogleAuthErrorHandler googleAuthErrorHandler() {
        return mock(GoogleAuthErrorHandler.class);
    };

    @Bean
    public GoogleAuthSuccessHandler googleAuthSuccessHandler() {
        return mock(GoogleAuthSuccessHandler.class);
    };

    @Bean
    public GoogleOAuth2UserService googleOAuth2UserService() {
        return mock(GoogleOAuth2UserService.class);
    };

    @Bean
    public GoogleOidcUserService googleOidcUserService() {
        return mock(GoogleOidcUserService.class);
    };

    @Bean
    public GoogleAccessResponseClient googleAccessResponseClient() {
        return mock(GoogleAccessResponseClient.class);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }
}
