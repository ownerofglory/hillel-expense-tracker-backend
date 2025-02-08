package ua.ithillel.expensetracker.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DateTimeField {
    @JsonProperty(value = "type")
    private final String type = "string";

    @JsonProperty(value = "description")
    private String description;

    @JsonCreator
    DateTimeField(@JsonProperty(value = "description") String description) {
        this.description = description;
    }
}
