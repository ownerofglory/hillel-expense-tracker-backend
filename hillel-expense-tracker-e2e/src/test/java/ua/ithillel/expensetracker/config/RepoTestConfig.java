package ua.ithillel.expensetracker.config;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import ua.ithillel.expensetracker.model.*;

import java.util.Properties;

@Configuration
public class RepoTestConfig {
    @Bean("sf")
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.h2.Driver");
        props.put("hibernate.connection.url", "jdbc:h2:mem:test_hibernate;INIT=runscript from 'classpath:init.sql'");
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

