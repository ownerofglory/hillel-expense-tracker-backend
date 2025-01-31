package ua.ithillel.expensetracker.client;

import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;

import java.util.List;

public interface ExternalCategoryClient {
    List<ExpenseCategoryDTO> getExternalCategories(Long userId);
}
