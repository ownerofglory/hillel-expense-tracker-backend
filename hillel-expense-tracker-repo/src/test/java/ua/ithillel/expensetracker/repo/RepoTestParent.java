package ua.ithillel.expensetracker.repo;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class RepoTestParent {
    protected EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setUpParent() {
        entityManagerFactory = Persistence.createEntityManagerFactory("expense-tracker-test-pu");
    }

    @AfterEach
    void tearDownParent() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
