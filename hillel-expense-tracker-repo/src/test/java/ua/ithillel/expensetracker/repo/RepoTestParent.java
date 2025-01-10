package ua.ithillel.expensetracker.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ua.ithillel.expensetracker.model.*;

import java.util.Properties;

public class RepoTestParent {
    protected EntityManagerFactory entityManagerFactory;
    protected SessionFactory sessionFactory;

    @BeforeEach
    void setUpParent() {
        createEntityManagerFactory();
        createSessionFactory();
    }

    @AfterEach
    void tearDownParent() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private void createEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("expense-tracker-test-pu");
    }

    private void createSessionFactory() {
        Configuration configuration = new Configuration();
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

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
}
