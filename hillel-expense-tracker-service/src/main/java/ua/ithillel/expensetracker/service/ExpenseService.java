package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.dto.ExpenseDTO;

import java.util.List;

public interface ExpenseService {
    List<ExpenseDTO> getExpensesByUserId(Long userId);
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);
}
