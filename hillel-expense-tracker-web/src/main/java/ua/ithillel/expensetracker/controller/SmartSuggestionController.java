package ua.ithillel.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.dto.SuggestionDTO;
import ua.ithillel.expensetracker.service.SmartExpenseService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/v1/suggest/expenses")
@RequiredArgsConstructor
public class SmartSuggestionController {
    private final SmartExpenseService smartExpenseService;

    @PostMapping(path = "/prompt", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody SuggestionDTO suggestionDTO) {
        ExpenseDTO expenseDTO = smartExpenseService.suggestExpenseByPrompt(suggestionDTO.getPrompt(), suggestionDTO.getUserId());

        return ResponseEntity.ok(expenseDTO);
    }

    @PostMapping(path = "/bill-scan")
    public ResponseEntity<ExpenseDTO> scanBill(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") Long userId) throws IOException {
        InputStream inputStream = file.getInputStream();


        ExpenseDTO suggestedExpenseByBillScan = smartExpenseService.suggestExpenseByBillScan(inputStream, userId);
        return ResponseEntity.ok(suggestedExpenseByBillScan);
    }
}
