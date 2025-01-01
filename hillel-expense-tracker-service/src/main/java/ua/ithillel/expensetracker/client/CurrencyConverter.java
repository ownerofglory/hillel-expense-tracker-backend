package ua.ithillel.expensetracker.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import ua.ithillel.expensetracker.dto.CurrenciesAvailableDTO;
import ua.ithillel.expensetracker.dto.CurrencyRatesDTO;

import java.io.IOException;

public interface CurrencyConverter {
    CurrenciesAvailableDTO getCurrenciesAvailable() throws IOException, InterruptedException;
    CurrencyRatesDTO getCurrencyRates(String currency) throws IOException, InterruptedException;
}
