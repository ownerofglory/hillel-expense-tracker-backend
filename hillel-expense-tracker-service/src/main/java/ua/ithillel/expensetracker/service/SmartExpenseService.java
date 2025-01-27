package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.ExpenseDTO;

import java.io.InputStream;

public interface SmartExpenseService {
    ExpenseDTO suggestExpenseByPrompt(String prompt, Long userId);
    ExpenseDTO suggestExpenseByBillScan(InputStream billScanInputStream, Long userId);
}
