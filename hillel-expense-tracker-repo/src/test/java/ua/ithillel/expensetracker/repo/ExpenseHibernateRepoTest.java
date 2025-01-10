package ua.ithillel.expensetracker.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.Expense;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExpenseHibernateRepoTest extends RepoTestParent {
    private ExpenseHibernateRepo expenseHibernateRepo;

    @BeforeEach
    void setUp() {
        expenseHibernateRepo = new ExpenseHibernateRepo(sessionFactory);
    }

    @Test
    void findTest_success() throws ExpenseTrackerPersistingException {
        Long id = 1L;
        Optional<Expense> expense = expenseHibernateRepo.find(id);

        assertNotNull(expense.get());
        assertNotNull(expense.get().getId());
        assertEquals(id, expense.get().getId());
    }

    @Test
    void saveTest_idNotNull() throws ExpenseTrackerPersistingException {
        Expense expense = new Expense();
        expense.setDescription("test description");
        expense.setAmount(30);

        Optional<Expense> saved = expenseHibernateRepo.save(expense);

        assertNotNull(saved.get());
        assertNotNull(saved.get().getId());
        assertEquals(saved.get().getDescription(), expense.getDescription());
        assertEquals(saved.get().getAmount(), expense.getAmount());
    }
}
