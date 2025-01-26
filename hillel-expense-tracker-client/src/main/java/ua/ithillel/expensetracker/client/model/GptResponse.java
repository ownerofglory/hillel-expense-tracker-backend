package ua.ithillel.expensetracker.client.model;

import lombok.Data;

@Data
public class GptResponse<T> {
    private T content;
}
