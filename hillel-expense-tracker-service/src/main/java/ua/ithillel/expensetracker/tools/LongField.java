package ua.ithillel.expensetracker.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LongField {
    @JsonProperty(value = "type")
    private final String type = "number";

    @JsonProperty(value = "description")
    private String description;

    @JsonCreator
    LongField(@JsonProperty(value = "description") String description) {
        this.description = description;
    }
}
