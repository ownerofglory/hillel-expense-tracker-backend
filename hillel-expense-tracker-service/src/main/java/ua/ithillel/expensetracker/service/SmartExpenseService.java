package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.exception.ServiceException;

import java.io.InputStream;

public interface SmartExpenseService {
    ExpenseDTO suggestExpenseByPrompt(String prompt, Long userId) throws ServiceException;
    ExpenseDTO suggestExpenseByBillScan(InputStream billScanInputStream, Long userId) throws ServiceException;
}
