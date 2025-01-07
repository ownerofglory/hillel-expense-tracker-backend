package ua.ithillel.expensetracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.model.Expense;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenseServiceDefaultTest {
    private ExpenseService expenseService;

    private ExpenseRepo expenseRepo;
    private ExpenseCategoryRepo expenseCategoryRepo;
    private UserRepo userRepo;
    private ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
//        userRepo = new FakeUserRepo();
        expenseRepo = mock(ExpenseRepo.class);
        expenseCategoryRepo = mock(ExpenseCategoryRepo.class);
        userRepo = mock(UserRepo.class);
//        expenseMapper = mock(ExpenseMapper.class);
        expenseMapper = ExpenseMapper.INSTANCE;

        expenseService = new ExpenseServiceDefault(expenseRepo, userRepo, expenseCategoryRepo, expenseMapper);
    }

    @Test
    void getExpensesByUserId_returnsExpenses() throws ExpenseTrackerPersistingException {
        Long testId = 1L;

        when(userRepo.find(anyLong())).thenReturn(Optional.of(new User()));
        when(expenseRepo.findByUser(any())).thenReturn(List.of(new Expense()));

        List<ExpenseDTO> expenses = expenseService.getExpensesByUserId(testId);

        assertNotNull(expenses);
        assertNotEquals(0, expenses.size());
    }

    @Test
    void getExpensesByUserId_nonExistingUserThrowsException() throws ExpenseTrackerPersistingException {
        Long testId = 9999999L;

        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> expenseService.getExpensesByUserId(testId));

    }
}
