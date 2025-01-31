package ua.ithillel.expensetracker.client.impl;

import ua.ithillel.expensetracker.client.ExternalCategoryClient;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;

import java.util.List;

//@Component("usa")
//@Primary
public class ExternalCategoryClientUSA implements ExternalCategoryClient {
    @Override
    public List<ExpenseCategoryDTO> getExternalCategories(Long userId) {
        return List.of();
    }
}
