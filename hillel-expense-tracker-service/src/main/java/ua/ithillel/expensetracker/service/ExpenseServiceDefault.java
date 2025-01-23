package ua.ithillel.expensetracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.model.Expense;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceDefault implements ExpenseService {
//    private static final Logger LOGGER = Logger.getLogger(ExpenseServiceDefault.class.getName());
//    private static final Logger LOGGER = LogManager.getLogger(ExpenseServiceDefault.class);
//    private static final Logger log = LoggerFactory.getLogger(ExpenseServiceDefault.class);

    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;
    private final ExpenseCategoryRepo categoryRepo;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseDTO> getExpensesByUserId(Long userId) {
        log.info("Get expense by id");
        log.debug("userId: " + userId);

//        LOGGER.fine("user id: " + userId);
        try {

            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow();

            List<Expense> expenseList = expenseRepo.findByUser(existingUser);

            return expenseList.stream()
                    .map(expenseMapper::expenseToExpenseDTO)
                    .toList();

        } catch (ExpenseTrackerPersistingException e) {
//            LOGGER.severe(e.getMessage());
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        log.info("createExpense");
        log.debug("expense: " + expenseDTO);
//        LOGGER.fine("expense: " + expenseDTO);
        try {
            Long userId = expenseDTO.getUserId();
            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow();

            Long categoryId = expenseDTO.getCategoryId();
            Optional<ExpenseCategory> expenseCategory = categoryRepo.find(categoryId);
            ExpenseCategory existingCategory = expenseCategory.orElseThrow();

            if (!existingUser.getCategories().contains(existingCategory)) {
                throw new RuntimeException("Category does not belong to user");
            }

            Expense expense = new Expense();
            expense.setCategory(existingCategory);
            expense.setUser(existingUser);
            expense.setAmount(expenseDTO.getAmount());
            expense.setDescription(expenseDTO.getDescription());

            Optional<Expense> saved = expenseRepo.save(expense);
            return expenseMapper.expenseToExpenseDTO(saved.get());


        } catch (ExpenseTrackerPersistingException e) {
            log.error(e.getMessage());
//            LOGGER.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
