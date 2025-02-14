package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.exception.ServiceException;

import java.util.List;

public interface CategoryService {
    List<ExpenseCategoryDTO> findAllCategoriesByUserId(Long userId) throws ServiceException;
    ExpenseCategoryDTO findCategoryById(Long id) throws ServiceException;
}
