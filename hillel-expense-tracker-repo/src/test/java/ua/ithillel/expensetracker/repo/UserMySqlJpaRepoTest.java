package ua.ithillel.expensetracker.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserMySqlJpaRepoTest extends RepoTestParent {
    private UserMySqlJpaRepo userMySqlJpaRepo;

    @BeforeEach
    void setUp() {
        userMySqlJpaRepo = new UserMySqlJpaRepo(entityManagerFactory);
    }

    @Test
    void findTest_userExists() throws ExpenseTrackerPersistingException {
        Long testId = 1L;
        Optional<User> user = userMySqlJpaRepo.find(testId);

        assertNotNull(user.orElse(null));
        assertEquals(testId, user.get().getId());
        assertNotNull(user.get().getCategories());
        assertNotNull(user.get().getExpenses());
    }

    @Test
    void findTest_userDoesNotExistReturnsNull() throws ExpenseTrackerPersistingException {
        Long testId = 8000000L;
        Optional<User> user = userMySqlJpaRepo.find(testId);
        assertNull(user.orElse(null));
    }

    @Test
    void saveTest_returnsUserWithId() throws ExpenseTrackerPersistingException {
        User testUser = new User();
        testUser.setFirstname("testUser");
        testUser.setLastname("testUser");
        testUser.setEmail("testUser@example.com");

        Optional<User> saved = userMySqlJpaRepo.save(testUser);

        assertTrue(saved.isPresent());
        assertNotNull(saved.get().getId());
        assertEquals(testUser.getFirstname(), saved.get().getFirstname());
        assertEquals(testUser.getLastname(), saved.get().getLastname());
        assertEquals(testUser.getEmail(), saved.get().getEmail());
    }

    @Test
    void saveTest_withCategoriesReturnsUserWithId() throws ExpenseTrackerPersistingException {
        User testUser = new User();
        testUser.setFirstname("testUser");
        testUser.setLastname("testUser");
        testUser.setEmail("testUser@example.com");
        testUser.setCategories(Set.of(new ExpenseCategory("testCategory", Set.of(), testUser)));

        Optional<User> saved = userMySqlJpaRepo.save(testUser);

        assertTrue(saved.isPresent());
        assertNotNull(saved.get().getId());
        assertEquals(testUser.getFirstname(), saved.get().getFirstname());
        assertEquals(testUser.getLastname(), saved.get().getLastname());
        assertEquals(testUser.getEmail(), saved.get().getEmail());
        assertEquals(testUser.getCategories(), saved.get().getCategories());
        saved.get()
                .getCategories()
                .forEach(c -> assertNotNull(c.getId()));
    }

}
