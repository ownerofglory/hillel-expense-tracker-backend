package ua.ithillel.expensetracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorisingResponseDTO {
    @JsonPropertyDescription("name of an expense category")
    @JsonProperty(required = true)
    private String categoryName;
    @JsonPropertyDescription("amount of money spent")
    @JsonProperty(required = true)
    private double amount;
    @JsonPropertyDescription("description of the expense")
    @JsonProperty(required = true)
    private String description;
    @JsonPropertyDescription("currency, if none specified 'EUR' should be used")
    @JsonProperty(required = true)
    private String currency;
    @JsonPropertyDescription("true if the gpt model was not able to categorise expense, e.g. due to irrelevant prompt")
    @JsonProperty(required = true)
    private Boolean refused;
    @JsonPropertyDescription("message for user if the gpt model was not able to categorise expense, e.g. due to irrelevant prompt")
    @JsonProperty(required = true)
    private String refusalReason;
}
