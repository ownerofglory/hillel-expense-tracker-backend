package ua.ithillel.expensetracker.repo;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ithillel.expensetracker.config.RepoTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepoTestConfig.class)
public class RepoTestParent {
    @Autowired
    @Qualifier("emf")
    protected EntityManagerFactory entityManagerFactory;
    @Autowired
    @Qualifier("sf")
    protected SessionFactory sessionFactory;

    @BeforeEach
    void setUpParent() {

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
}
