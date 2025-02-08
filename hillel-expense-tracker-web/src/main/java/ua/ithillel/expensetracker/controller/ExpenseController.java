package ua.ithillel.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.expensetracker.dto.ExpenseDTO;
import ua.ithillel.expensetracker.service.ExpenseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<List<ExpenseDTO>> getAll(@RequestParam(name = "userId") Long userId) {
        List<ExpenseDTO> expensesByUserId = expenseService.getExpensesByUserId(userId);

        return ResponseEntity.ok(expensesByUserId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO savedExpense = expenseService.createExpense(expenseDTO);

        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> editExpense(@PathVariable("id") Long id,
                                                  @RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO updateExpense = expenseService.updateExpense(id, expenseDTO);

        return ResponseEntity.ok(updateExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExpenseDTO> deleteExpense(@PathVariable("id") Long id) {
        ExpenseDTO expenseDTO = expenseService.deleteExpense(id);
        return ResponseEntity.ok(expenseDTO);
    }
}
