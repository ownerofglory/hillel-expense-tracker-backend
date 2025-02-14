package ua.ithillel.expensetracker.dto;

import lombok.Data;

@Data
public class SuggestionDTO {
    private String prompt;
    private Long userId;
}
