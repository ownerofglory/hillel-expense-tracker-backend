package ua.ithillel.expensetracker;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.KeyCredential;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.client.impl.AzureOpenAiGptClient;
import ua.ithillel.expensetracker.model.*;
import ua.ithillel.expensetracker.repo.*;
import ua.ithillel.expensetracker.service.SmartExpenseGptService;
import ua.ithillel.expensetracker.service.SmartExpenseService;
import ua.ithillel.expensetracker.util.Base64Converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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

        String openaiKey = System.getenv("OPENAI_KEY");
        OpenAIClient client = new OpenAIClientBuilder()
                .credential(new KeyCredential(openaiKey))
                .buildClient();

        GPTClient gptClient = new AzureOpenAiGptClient(client, new ObjectMapper());

        Base64Converter base64Converter = new Base64Converter();

        try (
                EntityManagerFactory entityManagerFactory
                        = Persistence.createEntityManagerFactory("expense-tracker-pu", jpaConfig);

                EntityManager entityManager = entityManagerFactory.createEntityManager(); // works with JPA context
                SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                InputStream resourceAsStream = Application.class.getClassLoader().getResourceAsStream("hofer-rechnung-300-euro.jpg");
        ) {
            UserRepo userRepo = new UserMySqlJpaRepo(entityManagerFactory);
            ExpenseRepo expenseRepo = new ExpenseHibernateRepo(sessionFactory);
            ExpenseCategoryRepo expenseCategoryRepo = new ExpenseCategoryHibernateRepo(sessionFactory);

            SmartExpenseService smartExpenseService = new SmartExpenseGptService(gptClient, expenseCategoryRepo, userRepo, base64Converter);

            Expense expense = smartExpenseService.suggestExpenseByBillScan(resourceAsStream, 2L);
//            Expense expense1 = smartExpenseService.suggestExpenseByPrompt("I went to the supermarket and spent 34.50", 2L);
//            Expense expense2 = smartExpenseService.suggestExpenseByPrompt("What is the capital of Nicaragua", 2L);

//            ExpenseService expenseService = new ExpenseServiceDefault(expenseRepo, userRepo, expenseCategoryRepo, ExpenseMapper.INSTANCE);
//
//            List<ExpenseDTO> expensesByUserId = expenseService.getExpensesByUserId(2L);
//
//            ExpenseDTO expenseDTO = new ExpenseDTO();
//            expenseDTO.setUserId(2L); // Jane Doe
//            expenseDTO.setCategoryId(5L); // Food
//            expenseDTO.setAmount(24);
//            expenseDTO.setDescription("Other food test test");
//
//            ExpenseDTO expense = expenseService.createExpense(expenseDTO);

            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
