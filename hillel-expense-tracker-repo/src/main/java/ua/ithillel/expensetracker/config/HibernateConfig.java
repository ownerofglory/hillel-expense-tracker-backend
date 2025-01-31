package ua.ithillel.expensetracker.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ua.ithillel.expensetracker.model.*;

import java.util.Properties;

@Configuration
@PropertySource("classpath:repo-config.properties")
public class HibernateConfig {
    @Value("${jdbc.user:def_user}")
    private String jdbcUser;
    @Value("${jdbc.password}")
    private String jdbcPassword;
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", jdbcDriver);
        props.put("hibernate.connection.url", jdbcUrl);
        props.put("hibernate.connection.username", jdbcUser);
        props.put("hibernate.connection.password", jdbcPassword);
        props.put("hibernate.current_session_context_class", "thread");

        configuration.setProperties(props);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(ExpenseCategory.class);
        configuration.addAnnotatedClass(Expense.class);
        configuration.addAnnotatedClass(ExpenseTag.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}
