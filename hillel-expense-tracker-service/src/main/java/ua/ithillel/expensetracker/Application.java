package ua.ithillel.expensetracker;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.service.CategoryService;
import ua.ithillel.expensetracker.service.ExpenseService;
import ua.ithillel.expensetracker.service.SmartExpenseService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Application {
    public static void main(String[] args) {
//        ExternalCategoryClient externalCategoryClient = new ExternalCategoryClientDefault();
//        CategoryService categoryService = new CategoryInMemoryService(externalCategoryClient);
//        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        try (InputStream image = Application.class.getClassLoader().getResourceAsStream("bill1.jpeg")) {
            ApplicationContext context = new AnnotationConfigApplicationContext("ua.ithillel.expensetracker");

            String region = System.getenv("REGION");
            CategoryService categoryService = context.getBean(CategoryService.class);
            CategoryService categoryService1 = (CategoryService) context.getBean("categoryService");
            List<ExpenseCategoryDTO> allCategoriesByUserId = categoryService1.findAllCategoriesByUserId(1L);


            ExpenseService expenseService = context.getBean(ExpenseService.class);

            SmartExpenseService smartExpenseService = context.getBean(SmartExpenseService.class);

            ExpenseDTO taxes = smartExpenseService.suggestExpenseByPrompt("I went to a bar and spent 30 euro for booze", 2L);
            ExpenseDTO supermarket = smartExpenseService.suggestExpenseByBillScan(image, 2L);

            System.out.println();

        } catch (ServiceException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
