package ua.ithillel.expensetracker.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.Map;

@Data
public class CurrenciesAvailableDTO {

    @JsonAnySetter
    private Map<String, String> availableCurrencies;

}
