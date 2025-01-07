package ua.ithillel.expensetracker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.model.*;
import ua.ithillel.expensetracker.repo.*;
import ua.ithillel.expensetracker.service.ExpenseService;
import ua.ithillel.expensetracker.service.ExpenseServiceDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
//        configuration.configure("META-INF/hibernate.cfg.xml");
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        props.put("hibernate.connection.url", String.format("jdbc:mysql://%s/%s", System.getenv("JDBC_DB_HOST"), System.getenv("JDBC_DB_NAME")));
        props.put("hibernate.connection.username", System.getenv("JDBC_USER"));
        props.put("hibernate.connection.password", System.getenv("JDBC_PASSWORD"));
        props.put("hibernate.current_session_context_class", "thread");

        configuration.setProperties(props);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(ExpenseCategory.class);
        configuration.addAnnotatedClass(Expense.class);
        configuration.addAnnotatedClass(ExpenseTag.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                .build();


        Map<String, String> jpaConfig = new HashMap<>();
        jpaConfig.put("jakarta.persistence.jdbc.url", String.format("jdbc:mysql://%s/%s", System.getenv("JDBC_DB_HOST"), System.getenv("JDBC_DB_NAME")));
        jpaConfig.put("jakarta.persistence.jdbc.user",System.getenv("JDBC_USER"));
        jpaConfig.put("jakarta.persistence.jdbc.password", System.getenv("JDBC_PASSWORD"));

        try (
                EntityManagerFactory entityManagerFactory
                        = Persistence.createEntityManagerFactory("expense-tracker-pu", jpaConfig);

                EntityManager entityManager = entityManagerFactory.createEntityManager(); // works with JPA context
                SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        ) {
            UserRepo userRepo = new UserMySqlJpaRepo(entityManagerFactory);
            ExpenseRepo expenseRepo = new ExpenseHibernateRepo(sessionFactory);
            ExpenseCategoryRepo expenseCategoryRepo = new ExpenseCategoryHibernateRepo(sessionFactory);

            ExpenseService expenseService = new ExpenseServiceDefault(expenseRepo, userRepo, expenseCategoryRepo, ExpenseMapper.INSTANCE);

            List<ExpenseDTO> expensesByUserId = expenseService.getExpensesByUserId(2L);

            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setUserId(2L); // Jane Doe
            expenseDTO.setCategoryId(5L); // Food
            expenseDTO.setAmount(24);
            expenseDTO.setDescription("Food test");

            ExpenseDTO expense = expenseService.createExpense(expenseDTO);

            System.out.println();
        }

    }
}
