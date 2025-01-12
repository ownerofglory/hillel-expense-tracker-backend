package ua.ithillel.expensetracker.client;

import ua.ithillel.expensetracker.dto.CurrenciesAvailableDTO;
import ua.ithillel.expensetracker.dto.CurrencyRatesDTO;

public interface CurrencyConverter {
    CurrenciesAvailableDTO getCurrenciesAvailable();
    CurrencyRatesDTO getCurrencyRates(String currency);
}
