package ua.ithillel.expensetracker.model;

import java.time.LocalDateTime;

public abstract class AbstractModel {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
