package ua.ithillel.expensetracker.model;

import lombok.Data;

@Data
public class GptResponse<T> {
    protected T content;
}
