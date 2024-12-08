package ua.ithillel.expensetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractModel {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
