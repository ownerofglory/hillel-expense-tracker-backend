package ua.ithillel.expensetracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.dto.PaginationDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.exception.NotFoundServiceException;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.model.*;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExpenseServiceDefault implements ExpenseService {
    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;
    private final ExpenseCategoryRepo categoryRepo;
    private final ExpenseMapper expenseMapper;

    @Override
    @PreAuthorize("#userId == authentication.principal.id || hasRole('ROLE_ADMIN')")
    public List<ExpenseDTO> getExpensesByUserId(Long userId) {
        log.info("Get expense by id");
        log.debug("userId: " + userId);

        try {

            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow(() -> new NotFoundServiceException("User not found"));

            List<Expense> expenseList = expenseRepo.findByUser(existingUser);

            return expenseList.stream()
                    .map(expenseMapper::expenseToExpenseDTO)
                    .toList();

        } catch (ExpenseTrackerPersistingException e) {
//            log.error(e.getMessage());
            throw new NotFoundServiceException(e.getMessage());
        }

    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    public PaginationDTO<List<ExpenseDTO>> getExpensesByUserId(Long userId, int page, int size) {
        try {

            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow(() -> new NotFoundServiceException("User not found"));

            PaginationReq paginationReq = new PaginationReq();
            paginationReq.setPage(page);
            paginationReq.setSize(size);
            PaginationResponse<List<Expense>> byUser = expenseRepo.findByUser(existingUser, paginationReq);

            List<ExpenseDTO> expenseDTOS = byUser.getData().stream()
                    .map(expenseMapper::expenseToExpenseDTO)
                    .toList();

            PaginationDTO<List<ExpenseDTO>> paginationDTO = new PaginationDTO<>();
            paginationDTO.setData(expenseDTOS);
            paginationDTO.setSize(size);
            paginationDTO.setPage(page);
            paginationDTO.setCount(byUser.getCount());

            return paginationDTO;

        } catch (ExpenseTrackerPersistingException e) {
//            log.error(e.getMessage());
            throw new NotFoundServiceException(e.getMessage());
        }

    }

    @Override
    @PreAuthorize("#expenseDTO.userId == authentication.principal.id")
    public ExpenseDTO createExpense(ua.ithillel.expensetracker.dto.ExpenseDTO expenseDTO) {
        log.info("createExpense");
        log.debug("expense: " + expenseDTO);
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
            throw new NotFoundServiceException(e.getMessage());
        }
    }

    @Override
    @PostAuthorize("returnObject.userId == authentication.principal.id")
    public ExpenseDTO getExpenseById(Long id) {
        try {
            Optional<Expense> expense = expenseRepo.find(id);
            Expense existingExpense = expense.orElseThrow(() -> new NotFoundServiceException("Expense not found"));

            return expenseMapper.expenseToExpenseDTO(existingExpense);
        } catch (ExpenseTrackerPersistingException e) {
            throw new NotFoundServiceException("Error while getting expense by id");
        }
    }

    @Override
    @PreAuthorize("#expenseDTO.userId == authentication.principal.id")
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        try {
            Optional<Expense> expenseOpt = expenseRepo.find(id);
            Expense expense = expenseOpt.orElseThrow(() -> new NotFoundServiceException("Expense not found"));

            User user = userRepo.find(expenseDTO.getUserId()).get();

            Long categoryId = expenseDTO.getCategoryId();
            ExpenseCategory expenseCategory = categoryRepo.find(categoryId).get();

            expense.setAmount(expenseDTO.getAmount());
            expense.setDescription(expenseDTO.getDescription());
            expense.setCategory(expenseCategory);
            expense.setUser(user);


            Optional<Expense> saved = expenseRepo.save(expense);
            Expense udpdatedExpense = saved.orElseThrow(() -> new ServiceException("Expense could not be updated"));

            return expenseMapper.expenseToExpenseDTO(udpdatedExpense);
        } catch (ExpenseTrackerPersistingException e) {
            throw new ServiceException("Error while updating expense by id: " + e.getMessage());
        }
    }

    @Override
    public ExpenseDTO deleteExpense(Long id) {
        try {
            Optional<Expense> expense = expenseRepo.find(id);
            Expense existingExpense = expense.orElseThrow(() -> new NotFoundServiceException("Expense not found"));

            Optional<Expense> deleted = expenseRepo.delete(existingExpense);
            Expense deletedExpense = deleted.orElseThrow(() -> new ServiceException("Expense could not be deleted"));

            return expenseMapper.expenseToExpenseDTO(deletedExpense);

        } catch (ExpenseTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }
}
