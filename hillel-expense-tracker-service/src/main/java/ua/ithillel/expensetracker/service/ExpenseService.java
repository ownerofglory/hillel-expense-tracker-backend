package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {
    List<ExpenseDTO> getExpensesByUserId(Long userId);
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);
    ExpenseDTO getExpenseById(Long id);
    ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO);
    ExpenseDTO deleteExpense(Long id);
}
