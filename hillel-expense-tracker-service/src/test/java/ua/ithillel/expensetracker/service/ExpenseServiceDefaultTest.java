package ua.ithillel.expensetracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.model.Expense;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

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

        List<ua.ithillel.expensetracker.dto.ExpenseDTO> expenses = expenseService.getExpensesByUserId(testId);

        assertNotNull(expenses);
        assertNotEquals(0, expenses.size());
    }

    @Test
    void getExpensesByUserId_whenRepoFindByUserThrowsExceptionThrowsException() throws ExpenseTrackerPersistingException {
        Long testId = 1L;

        when(userRepo.find(anyLong())).thenReturn(Optional.of(new User()));
        when(expenseRepo.findByUser(any())).thenThrow(new ExpenseTrackerPersistingException(null, "", null));

        assertThrows(RuntimeException.class, () -> expenseService.getExpensesByUserId(testId));
    }

    @Test
    void getExpensesByUserId_whenRepoThrowsExceptionThrowsException() throws ExpenseTrackerPersistingException {
        Long testId = 1L;

        when(userRepo.find(anyLong())).thenThrow(new ExpenseTrackerPersistingException(null, "", null));

        assertThrows(RuntimeException.class, () -> expenseService.getExpensesByUserId(testId));
    }

    @Test
    void getExpensesByUserId_nonExistingUserThrowsException() throws ExpenseTrackerPersistingException {
        Long testId = 9999999L;

        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> expenseService.getExpensesByUserId(testId));

    }

    @Test
    void createExpense_success() throws ExpenseTrackerPersistingException {
        ua.ithillel.expensetracker.dto.ExpenseDTO testExpense = new ua.ithillel.expensetracker.dto.ExpenseDTO();
        testExpense.setCategoryId(1L);
        testExpense.setDescription("description");
        testExpense.setUserId(1L);

        User user = new User();
        user.setId(1L);
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1L);
        user.setCategories(Set.of(expenseCategory));

        Expense mockExpense = new Expense();
        mockExpense.setId(1000L);
        mockExpense.setDescription(testExpense.getDescription());

        when(userRepo.find(anyLong())).thenReturn(Optional.of(user));
        when(expenseCategoryRepo.find(anyLong())).thenReturn(Optional.of(expenseCategory));
        when(expenseRepo.save(any())).thenReturn(Optional.of(mockExpense));


        ua.ithillel.expensetracker.dto.ExpenseDTO expense = expenseService.createExpense(testExpense);
        assertNotNull(expense);
        assertNotNull(expense.getId());
    }

    @Test
    void createExpense_nonExistingUserThrowsException() throws ExpenseTrackerPersistingException {
        ua.ithillel.expensetracker.dto.ExpenseDTO testExpense = new ua.ithillel.expensetracker.dto.ExpenseDTO();
        testExpense.setCategoryId(1L);
        testExpense.setDescription("description");
        testExpense.setUserId(1L);

        User user = new User();
        user.setId(1L);
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1L);
        user.setCategories(Set.of(expenseCategory));

        when(userRepo.find(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> expenseService.createExpense(testExpense));
    }

    @Test
    void createExpense_nonExistingCategoryThrowsException() throws ExpenseTrackerPersistingException {
        ua.ithillel.expensetracker.dto.ExpenseDTO testExpense = new ua.ithillel.expensetracker.dto.ExpenseDTO();
        testExpense.setCategoryId(1L);
        testExpense.setDescription("description");
        testExpense.setUserId(1L);

        User user = new User();
        user.setId(1L);
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1L);
        user.setCategories(Set.of(expenseCategory));

        when(userRepo.find(anyLong())).thenReturn(Optional.of(user));
        when(expenseCategoryRepo.find(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> expenseService.createExpense(testExpense));
    }

    @Test
    void createExpense_categoriesDonNotMatch() throws ExpenseTrackerPersistingException {
        ua.ithillel.expensetracker.dto.ExpenseDTO testExpense = new ua.ithillel.expensetracker.dto.ExpenseDTO();
        testExpense.setCategoryId(1L);
        testExpense.setDescription("description");
        testExpense.setUserId(1L);

        User user = new User();
        user.setId(1L);
        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1L);
        user.setCategories(Set.of(expenseCategory));

        Expense mockExpense = new Expense();
        mockExpense.setId(1000L);
        mockExpense.setDescription(testExpense.getDescription());

        ExpenseCategory mockCategory = new ExpenseCategory();


        when(userRepo.find(anyLong())).thenReturn(Optional.of(user));
        when(expenseCategoryRepo.find(anyLong())).thenReturn(Optional.of(mockCategory));


        assertThrows(RuntimeException.class, () -> expenseService.createExpense(testExpense));
    }

}
