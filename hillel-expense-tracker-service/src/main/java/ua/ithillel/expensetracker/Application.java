package ua.ithillel.expensetracker;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptMessageContent;
import ua.ithillel.expensetracker.model.GptToolResponse;
import ua.ithillel.expensetracker.service.CategoryService;
import ua.ithillel.expensetracker.service.ExpenseService;
import ua.ithillel.expensetracker.service.SmartAgentService;
import ua.ithillel.expensetracker.service.SmartExpenseService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

public class Application {
    public static void main(String[] args) {
        try (InputStream image = Application.class.getClassLoader().getResourceAsStream("bill1.jpeg")) {
            ApplicationContext context = new AnnotationConfigApplicationContext("ua.ithillel.expensetracker");

            SmartAgentService smartAgentService = context.getBean(SmartAgentService.class);

            GptMessage gptMessage = new GptMessage();
            gptMessage.setRole(GptMessage.ROLE_USER);
            GptMessageContent gptMessageContent = new GptMessageContent();
            gptMessageContent.setType(GptMessageContent.CONTENT_TYPE_TEXT);
            gptMessageContent.setTextContent("What are my 3 biggest expense for feabruary 2025, user id is 1");
//            gptMessageContent.setTextContent("tell me a joke");
            gptMessage.setContent(gptMessageContent);

            List<GptMessage> gptMessages = List.of(gptMessage);
            Stream<GptToolResponse> chatCompletionWithTools = smartAgentService.getChatCompletionWithTools(gptMessages, 1L);
            chatCompletionWithTools.forEach(r -> {
                System.out.println(r);
            });

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
