package ua.ithillel.expensetracker.tools.types;

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
    public LongField(@JsonProperty(value = "description") String description) {
        this.description = description;
    }
}
