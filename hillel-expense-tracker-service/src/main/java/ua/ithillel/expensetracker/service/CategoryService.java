package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;

import java.util.List;

public interface CategoryService {
    List<ExpenseCategoryDTO> findAllCategoriesByUserId(Long userId);
}
