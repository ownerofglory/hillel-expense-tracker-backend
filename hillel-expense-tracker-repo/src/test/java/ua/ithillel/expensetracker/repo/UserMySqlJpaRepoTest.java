package ua.ithillel.expensetracker.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMySqlJpaRepoTest extends RepoTestParent {
    private UserMySqlJpaRepo userMySqlJpaRepo;

    @BeforeEach
    void setUp() {
        userMySqlJpaRepo = new UserMySqlJpaRepo(entityManagerFactory);
    }

    @Test
    void findTest() throws ExpenseTrackerPersistingException {
        Long testId = 1L;
        Optional<User> user = userMySqlJpaRepo.find(testId);

        assertNotNull(user.orElse(null));
        assertEquals(testId, user.get().getId());
    }
}
