package ua.ithillel.expensetracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.ithillel.expensetracker.client.GPTClient;
import ua.ithillel.expensetracker.model.GptResponse;
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

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class SmartExpenseGptServiceTest {
    private SmartExpenseGptService smartExpenseGptService;

    @Mock
    private GPTClient gptClientMock;
    @Mock
    private ExpenseCategoryRepo expenseCategoryRepoMock;
    @Mock
    private UserRepo userRepoMock;
    @Mock
    private Base64Converter base64ConverterMock;
    @Mock
    private ImageConvertor imageConvertorMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        smartExpenseGptService = new SmartExpenseGptService(gptClientMock, expenseCategoryRepoMock, userRepoMock, base64ConverterMock, imageConvertorMock);
    }

    @Test
    void suggestExpenseByPromptTest_success() throws ExpenseTrackerPersistingException, ServiceException {
        User mockUser = new User();
        when(userRepoMock.find(anyLong())).thenReturn(Optional.of(mockUser));

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1000L);
        expenseCategory.setName("test");
        when(expenseCategoryRepoMock.findByUser(any())).thenReturn(List.of(expenseCategory));

        GptResponse<CategorisingResponseDTO> mockCompletion = new GptResponse<>();
        CategorisingResponseDTO content = new CategorisingResponseDTO();
        content.setRefused(false);
        content.setCategoryName("test");
        mockCompletion.setContent(content);
        when(gptClientMock.getChatCompletionWithResponseType(anyList(), eq(CategorisingResponseDTO.class))).thenReturn(mockCompletion);

        ExpenseDTO expenseDTO = smartExpenseGptService.suggestExpenseByPrompt("test prompt", 1000L);
        assertNotNull(expenseDTO);
    }

    @Test
    void suggestExpenseByPromptTest_modelRefuses() throws ExpenseTrackerPersistingException {
        User mockUser = new User();
        when(userRepoMock.find(anyLong())).thenReturn(Optional.of(mockUser));

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1000L);
        expenseCategory.setName("test");
        when(expenseCategoryRepoMock.findByUser(any())).thenReturn(List.of(expenseCategory));

        GptResponse<CategorisingResponseDTO> mockCompletion = new GptResponse<>();
        CategorisingResponseDTO content = new CategorisingResponseDTO();
        content.setRefused(true);
        content.setRefusalReason("test");
        when(gptClientMock.getChatCompletionWithResponseType(anyList(), eq(CategorisingResponseDTO.class))).thenReturn(mockCompletion);

        assertThrows(RuntimeException.class, () -> {
            smartExpenseGptService.suggestExpenseByPrompt("test prompt", 1000L);
        });
    }

    @Test
    void suggestExpenseByPromptTest_userNotFound() throws ExpenseTrackerPersistingException {
        when(userRepoMock.find(anyLong())).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> smartExpenseGptService.suggestExpenseByPrompt("test prompt", 1000L));
    }


    @Test
    void suggestExpenseByBillScanTest_success() throws ExpenseTrackerPersistingException, ServiceException {
        User mockUser = new User();
        when(userRepoMock.find(anyLong())).thenReturn(Optional.of(mockUser));

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1000L);
        expenseCategory.setName("test");
        when(expenseCategoryRepoMock.findByUser(any())).thenReturn(List.of(expenseCategory));

        GptResponse<CategorisingResponseDTO> mockCompletion = new GptResponse<>();
        CategorisingResponseDTO content = new CategorisingResponseDTO();
        content.setRefused(false);
        content.setCategoryName("test");
        mockCompletion.setContent(content);
        when(gptClientMock.getChatCompletionWithResponseType(anyList(), eq(CategorisingResponseDTO.class))).thenReturn(mockCompletion);

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        ExpenseDTO expenseDTO = smartExpenseGptService.suggestExpenseByBillScan(testInputStream, 1000L);
        assertNotNull(expenseDTO);
    }


    @Test
    void suggestExpenseByBillScanTest_userNotFound() throws ExpenseTrackerPersistingException {
        when(userRepoMock.find(anyLong())).thenReturn(Optional.empty());

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        assertThrows(ServiceException.class, () -> smartExpenseGptService.suggestExpenseByBillScan(testInputStream, 1000L));
    }

    @Test
    void suggestExpenseByBillScanTest_modelRefuses() throws ExpenseTrackerPersistingException {
        User mockUser = new User();
        when(userRepoMock.find(anyLong())).thenReturn(Optional.of(mockUser));

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1000L);
        expenseCategory.setName("test");
        when(expenseCategoryRepoMock.findByUser(any())).thenReturn(List.of(expenseCategory));

        GptResponse<CategorisingResponseDTO> mockCompletion = new GptResponse<>();
        CategorisingResponseDTO content = new CategorisingResponseDTO();
        content.setRefused(true);
        content.setRefusalReason("test");
        mockCompletion.setContent(content);
        when(gptClientMock.getChatCompletionWithResponseType(anyList(), eq(CategorisingResponseDTO.class))).thenReturn(mockCompletion);

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        assertThrows(ServiceException.class, () -> smartExpenseGptService.suggestExpenseByBillScan(testInputStream, 1000L));
    }

}
