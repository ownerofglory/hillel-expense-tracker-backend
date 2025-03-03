package ua.ithillel.expensetracker.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("!test")
public class JpaConfig {
//    @Bean(name = "jpaEMF")
    public EntityManagerFactory entityManagerFactory() {
        Map<String, String> jpaConfig = new HashMap<>();
        jpaConfig.put("jakarta.persistence.jdbc.url", String.format("jdbc:mysql://%s/%s", System.getenv("JDBC_DB_HOST"), System.getenv("JDBC_DB_NAME")));
        jpaConfig.put("jakarta.persistence.jdbc.user",System.getenv("JDBC_USER"));
        jpaConfig.put("jakarta.persistence.jdbc.password", System.getenv("JDBC_PASSWORD"));

        return Persistence.createEntityManagerFactory("expense-tracker-pu", jpaConfig);
    }
}
