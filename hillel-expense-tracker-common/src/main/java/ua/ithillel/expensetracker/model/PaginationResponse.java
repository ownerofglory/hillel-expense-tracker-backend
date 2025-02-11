package ua.ithillel.expensetracker.model;

import lombok.Data;

@Data
public class PaginationResponse<T> {
    private int page;
    private int size;
    private long count;
    private T data;
}
