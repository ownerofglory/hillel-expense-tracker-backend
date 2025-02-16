package ua.ithillel.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.expensetracker.dto.ExpenseCategoryDTO;
import ua.ithillel.expensetracker.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<List<ExpenseCategoryDTO>> getAll(@RequestParam(name = "userId") Long userId) {
        List<ExpenseCategoryDTO> allCategoriesByUserId = categoryService.findAllCategoriesByUserId(userId);
        return ResponseEntity.ok(allCategoriesByUserId);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<ExpenseCategoryDTO> getById(@PathVariable(name = "id") Long id) {
        ExpenseCategoryDTO categoryById = categoryService.findCategoryById(id);
        return ResponseEntity.ok(categoryById);
    }

}
