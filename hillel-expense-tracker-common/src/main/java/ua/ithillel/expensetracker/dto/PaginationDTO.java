package ua.ithillel.expensetracker.dto;

import lombok.Data;

@Data
public class PaginationDTO<T> {
    private int page;
    private int size;
    private long count;
    private T data;
}
