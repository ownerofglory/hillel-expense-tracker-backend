package ua.ithillel.expensetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.client.ExternalCategoryClient;
import ua.ithillel.expensetracker.dao.UserDao;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.exception.DaoException;
import ua.ithillel.expensetracker.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("categoryService")
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class CategoryInMemoryService implements CategoryService {

    private List<ExpenseCategoryDTO> categories;


    @Autowired
    private UserDao userDao;


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
        try {
            Optional<User> user = userDao.find(userId);

            List<ExpenseCategoryDTO> externalCategories = externalCategoryClient.getExternalCategories(userId);

            List<ExpenseCategoryDTO> list = new ArrayList<>(categories.stream()
                    .filter(expenseCategoryDTO -> userId.equals(expenseCategoryDTO.getUserId()))
                    .toList());

            list.addAll(externalCategories);
            return list;
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}
