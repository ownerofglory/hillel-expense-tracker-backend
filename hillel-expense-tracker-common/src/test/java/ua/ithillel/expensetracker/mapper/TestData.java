package ua.ithillel.expensetracker.mapper;

import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;

public class TestData {
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String EMAIL = "john.doe@example.com";
    public static final double AMOUNT = 100.0;
    public static final String DESCRIPTION = "This is a test expense";

    public static final String EXPENSE_CAT_NAME = "Utilities";

    public static final User USER = new User(FIRST_NAME, LAST_NAME, EMAIL);
    public static final UserDTO USER_DTO = new UserDTO(FIRST_NAME, LAST_NAME, EMAIL);

    public static final ExpenseCategory EXPENSE_CATEGORY = new ExpenseCategory(EXPENSE_CAT_NAME);
    public static final ExpenseCategoryDTO EXPENSE_CATEGORY_DTO = new ExpenseCategoryDTO(EXPENSE_CAT_NAME);
}
