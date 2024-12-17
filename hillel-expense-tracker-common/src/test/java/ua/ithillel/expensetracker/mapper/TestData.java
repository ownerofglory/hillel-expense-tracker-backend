package ua.ithillel.expensetracker.mapper;

import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.dto.ExpenseTagDTO;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.ExpenseTag;
import ua.ithillel.expensetracker.model.ExpenseTagColor;
import ua.ithillel.expensetracker.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestData {
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String EMAIL = "john.doe@example.com";
    public static final double AMOUNT = 100.0;
    public static final String DESCRIPTION = "This is a test expense";

    public static final String EXPENSE_CAT_NAME = "Utilities";
    public static final Set<ExpenseTagDTO> TAGS = Collections.emptySet();

    public static final User USER = new User(FIRST_NAME, LAST_NAME, EMAIL, new HashSet<>(), new HashSet<>(), new HashSet<>());
    public static final UserDTO USER_DTO = new UserDTO(FIRST_NAME, LAST_NAME, EMAIL);

    public static final ExpenseCategory EXPENSE_CATEGORY = new ExpenseCategory(EXPENSE_CAT_NAME);
    public static final ExpenseCategoryDTO EXPENSE_CATEGORY_DTO = new ExpenseCategoryDTO(EXPENSE_CAT_NAME);

    public static final ExpenseTagColor TAG_COLOR = ExpenseTagColor.CORAL;
    public static final String TAG_NAME = "test";
    public static final ExpenseTag TAG = new ExpenseTag(TAG_NAME, TAG_COLOR, null, null);
    public static final ExpenseTagDTO TAG_DTO = new ExpenseTagDTO(TAG_NAME, TAG_COLOR.getHexCode(), TAG_COLOR.getName());
}
