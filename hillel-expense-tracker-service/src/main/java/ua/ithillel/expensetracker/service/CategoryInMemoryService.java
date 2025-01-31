package ua.ithillel.expensetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.client.ExternalCategoryClient;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;

import java.util.ArrayList;
import java.util.List;

@Service("categoryService")
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class CategoryInMemoryService implements CategoryService {
    private final List<ExpenseCategoryDTO> categories;

    @Autowired
//    @Qualifier("usa")
    private final ExternalCategoryClient externalCategoryClient;

    public CategoryInMemoryService() {
        this.externalCategoryClient = null;
        this.categories = new ArrayList<>() {{
            add(new ExpenseCategoryDTO("test1", 1L));
            add(new ExpenseCategoryDTO("test2", 1L));
            add(new ExpenseCategoryDTO("test3", 2L));
        }};
    }

    public CategoryInMemoryService(ExternalCategoryClient externalCategoryClient) {
        this.externalCategoryClient = externalCategoryClient;
        this.categories = new ArrayList<>() {{
           add(new ExpenseCategoryDTO("test1", 1L));
           add(new ExpenseCategoryDTO("test2", 1L));
           add(new ExpenseCategoryDTO("test3", 2L));
        }};
    }

    @Override
    public List<ExpenseCategoryDTO> findAllCategoriesByUserId(Long userId) {
        List<ExpenseCategoryDTO> externalCategories = externalCategoryClient.getExternalCategories(userId);

        List<ExpenseCategoryDTO> list = new ArrayList<>(categories.stream()
                .filter(expenseCategoryDTO -> userId.equals(expenseCategoryDTO.getUserId()))
                .toList());

        list.addAll(externalCategories);
        return list;
    }
}
