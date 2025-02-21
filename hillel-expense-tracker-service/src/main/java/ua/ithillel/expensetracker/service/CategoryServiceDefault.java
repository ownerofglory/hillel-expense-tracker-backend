package ua.ithillel.expensetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.mapper.ExpenseCategoryMapper;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class CategoryServiceDefault implements CategoryService {
    private final ExpenseCategoryRepo expenseCategoryRepo;
    private final UserRepo userRepo;
    private final ExpenseCategoryMapper expenseCategoryMapper;

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    public List<ExpenseCategoryDTO> findAllCategoriesByUserId(Long userId) throws ServiceException {
        try {
            Optional<User> userOpt = userRepo.find(userId);
            User user = userOpt.orElseThrow(() -> new ServiceException("User not found"));

            List<ExpenseCategory> categories = expenseCategoryRepo.findByUser(user);

            return categories.stream()
                    .map(expenseCategoryMapper::expenseCategoryToDTO)
                    .toList();

        } catch (ExpenseTrackerPersistingException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @PostAuthorize("#returnObject.userId == authentication.principal.id")
    public ExpenseCategoryDTO findCategoryById(Long id) throws ServiceException {
        try {
            Optional<ExpenseCategory> expenseCategoryOpt = expenseCategoryRepo.find(id);
            ExpenseCategory expenseCategory = expenseCategoryOpt.orElseThrow(() -> new ServiceException("Category not found"));


            return expenseCategoryMapper.expenseCategoryToDTO(expenseCategory);
        } catch (ExpenseTrackerPersistingException e) {
            throw new RuntimeException(e);
        }
    }
}
