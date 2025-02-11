package ua.ithillel.expensetracker.model;

import lombok.Data;

@Data
public class PaginationReq {
    private int page;
    private int size;
}
