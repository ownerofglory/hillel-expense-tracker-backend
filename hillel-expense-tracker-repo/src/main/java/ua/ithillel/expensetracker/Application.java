package ua.ithillel.expensetracker;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.*;
import ua.ithillel.expensetracker.repo.ExpenseHibernateRepo;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserMySqlJpaRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.*;

/*
 Dear reviewer,
 this class is used for demo purposes as part of Java Pro course by Hillel.
 Please ignore if it doesn't comply with the project's conventions
 */
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
            
            Optional<User> userOpt = userRepo.find(1L);
            User user = userOpt.get();

            Optional<User> byEmail = userRepo.findByEmail("jane.smith@example.com");
            User jane = byEmail.get();

            List<Expense> janesExpenses = expenseRepo.findByUser(jane);



//            User userEntity = new User();
//            userEntity.setFirstname("bbb");
//            userEntity.setLastname("bbb");
//            userEntity.setEmail("bbb@user.com");
//
//            userEntity.setCategories(Set.of(
//                    new ExpenseCategory("Food"),
//                    new ExpenseCategory("Transportation"),
//                    new ExpenseCategory("Entertainment"),
//                    new ExpenseCategory("Health")
//            ));
//
//
//            userRepo.save(userEntity);









            System.out.println();







//            UserEntity user = entityManager.find(UserEntity.class, 1L);
//
//            UserEntity userEntity = new UserEntity();
//            userEntity.setFirstName("Java");
//            userEntity.setLastName("Pro");
//            userEntity.setEmail("java@pro.com");
//
//            entityManager.getTransaction().begin(); // BEGIN
//
//            // put entity into persistence context
//            entityManager.persist(userEntity);
//
//            userEntity.setLastName("Programming");
//
//            entityManager.flush();
//
//            entityManager.getTransaction().commit(); // COMMIT;



            System.out.println();
        } catch (ExpenseTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
        ;
    }
}
