package ua.ithillel.expensetracker.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetExpensesBetweenDateToolParams {
    @JsonProperty("userId")
    private LongField userId = new LongField("ID of the user in the database");
    @JsonProperty("from")
    private DateTimeField from = new DateTimeField("Date and time from in format yyyy-MM-ddTHH:mm:ss");
    @JsonProperty("to")
    private DateTimeField to = new DateTimeField("Date and time to in format yyyy-MM-ddTHH:mm:ss");
}
