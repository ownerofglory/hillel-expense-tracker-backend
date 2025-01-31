package ua.ithillel.expensetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.client.model.GptMessage;
import ua.ithillel.expensetracker.client.model.GptMessageContent;
import ua.ithillel.expensetracker.client.model.GptResponse;
import ua.ithillel.expensetracker.dto.CategorisingResponseDTO;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.model.ExpenseCategory;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseCategoryRepo;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.util.Base64Converter;
import ua.ithillel.expensetracker.util.ImageConvertor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SmartExpenseGptService implements SmartExpenseService {
    private final GPTClient gptClient;
    private final ExpenseCategoryRepo expenseCategoryRepo;
    private final UserRepo userRepo;
    private final Base64Converter base64Converter;
    private final ImageConvertor imageConvertor;

    @Override
    public ExpenseDTO suggestExpenseByPrompt(String prompt, Long userId) throws ServiceException {
        try {
            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow(() -> new ServiceException("User not found"));

            List<ExpenseCategory> categories = expenseCategoryRepo.findByUser(existingUser);
            String categoriesStr = categories.stream()
                    .map(c -> MessageFormat.format("{0} | {1}", c.getId(), c.getName()))
                    .reduce("id | category_name\n", (acc, entry) -> acc + entry + "\n");

            // create chat completion context
            String userStr = MessageFormat.format("{0}: {1} {2}", existingUser.getId(), existingUser.getFirstname(), existingUser.getLastname());
            String gptContextTemplate = getContext();
            String currentDateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            String gptContext = MessageFormat.format(gptContextTemplate, userStr, currentDateTimeStr,  categoriesStr);

            // prepare GPT messages
            GptMessage systemMessage = createGptTextMessage("system", gptContext);
            GptMessage gptUserMessage = createGptTextMessage("user", prompt);

            List<GptMessage> messages = List.of(systemMessage, gptUserMessage);

            GptResponse<CategorisingResponseDTO> chatCompletionWithResponseType = gptClient.getChatCompletionWithResponseType(messages, CategorisingResponseDTO.class);

            CategorisingResponseDTO categorisingContent = chatCompletionWithResponseType.getContent();
            if (categorisingContent.getRefused()) {
                throw new ServiceException("Unable to categorise expense: " + categorisingContent.getRefusalReason());
            }

            String categoryName = categorisingContent.getCategoryName();

            Optional<ExpenseCategory> foundCategory = categories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                    .findFirst();

            ExpenseDTO expense = new ExpenseDTO();
            expense.setUserId(userId);
            expense.setDescription(categorisingContent.getDescription());
            ExpenseCategory chosenCategory = foundCategory.orElseThrow(() -> new ServiceException("Category not found"));
            expense.setCategoryId(chosenCategory.getId());

            return expense;
        } catch (ExpenseTrackerPersistingException e) {
            throw new ServiceException("Unable categorise expense: " + e.getMessage());
        } catch (IOException e) {
            throw new ServiceException("Unable load chat completion context");
        }
    }

    @Override
    public ExpenseDTO suggestExpenseByBillScan(InputStream billScanInputStream, Long userId) throws ServiceException {
        try {
            Optional<User> user = userRepo.find(userId);
            User existingUser = user.orElseThrow(() -> new ServiceException("User not found"));

            List<ExpenseCategory> categories = expenseCategoryRepo.findByUser(existingUser);
            String categoriesStr = categories.stream()
                    .map(c -> MessageFormat.format("{0} | {1}", c.getId(), c.getName()))
                    .reduce("id | category_name\n", (acc, entry) -> acc + entry + "\n");

            // create chat completion context
            String userStr = MessageFormat.format("{0}: {1} {2}", existingUser.getId(), existingUser.getFirstname(), existingUser.getLastname());
            String gptContextTemplate = getContext();
            String currentDateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            String gptContext = MessageFormat.format(gptContextTemplate, userStr, currentDateTimeStr,  categoriesStr);

            // prepare GPT messages
            GptMessage systemMessage = createGptTextMessage("system", gptContext);
            GptMessage gptUserMessage = createGptTextMessage("user", "Please categorise the expense by the image of the provided bill");

            InputStream compressedInputStream = imageConvertor.compressImage(billScanInputStream);
            InputStream grayScaleInputStream = imageConvertor.convertImageToGrayscale(compressedInputStream);
            String base64Image = base64Converter.encodeToString(grayScaleInputStream);
            GptMessage gptUserImageMessage = createGptImageMessage("user", base64Image);

            List<GptMessage> messages = List.of(systemMessage, gptUserMessage, gptUserImageMessage);

            GptResponse<CategorisingResponseDTO> chatCompletionWithResponseType = gptClient.getChatCompletionWithResponseType(messages, CategorisingResponseDTO.class);

            CategorisingResponseDTO categorisingContent = chatCompletionWithResponseType.getContent();
            if (categorisingContent.getRefused()) {
                throw new ServiceException("Unable to categorise expense: " + categorisingContent.getRefusalReason());
            }

            String categoryName = categorisingContent.getCategoryName();

            Optional<ExpenseCategory> foundCategory = categories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                    .findFirst();

            ExpenseDTO expense = new ExpenseDTO();
            expense.setUserId(userId);
            expense.setAmount(categorisingContent.getAmount());
            expense.setDescription(categorisingContent.getDescription());
            ExpenseCategory chosenCategory = foundCategory.orElseThrow(() -> new ServiceException("Category not found"));
            expense.setCategoryId(chosenCategory.getId());

            return expense;
        } catch (IOException e) {
            throw new ServiceException("Unable to categorise expense. Error when converting the image: " + e.getMessage());
        } catch (ExpenseTrackerPersistingException e) {
            throw new ServiceException("Unable to categorise expense: " + e.getMessage());
        }
    }

    private GptMessage createGptTextMessage(String role, String text) {
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType("text");
        gptMessageContent.setTextContent(text);
        return new GptMessage(role, gptMessageContent);
    }

    private GptMessage createGptImageMessage(String role, String imageUrl) {
        GptMessageContent gptMessageContent = new GptMessageContent();
        gptMessageContent.setType("image_url");
        gptMessageContent.setImageUrl("data:image/jpeg;base64," + imageUrl);
        return new GptMessage(role, gptMessageContent);
    }

    private String getContext() throws IOException {
        try (
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("gpt-context.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
        ) {
            return br.lines()
                    .reduce((acc, line) -> acc + line)
                    .orElse("");
        }
    }
}
