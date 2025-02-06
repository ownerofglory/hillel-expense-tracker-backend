package ua.ithillel.expensetracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ua.ithillel.expensetracker.util.Base64Converter;
import ua.ithillel.expensetracker.util.ImageConvertor;

@Configuration
public class ServiceConfig {
    @Bean
    public Base64Converter base64Converter() {
        return new Base64Converter();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ImageConvertor imageConvertor() {
        return new ImageConvertor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        return objectMapper;
    }
}
