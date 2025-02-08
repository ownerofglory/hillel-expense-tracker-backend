package ua.ithillel.expensetracker.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetExpensesBetweenDatesArgs {
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("from")
    private LocalDateTime from;
    @JsonProperty("to")
    private LocalDateTime to;
}
