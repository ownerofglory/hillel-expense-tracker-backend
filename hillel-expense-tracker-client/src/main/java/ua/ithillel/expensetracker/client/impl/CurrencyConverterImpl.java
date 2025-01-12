package ua.ithillel.expensetracker.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.ithillel.expensetracker.client.CurrencyConverter;
import ua.ithillel.expensetracker.client.exception.CurrencyConverterException;
import ua.ithillel.expensetracker.dto.CurrenciesAvailableDTO;
import ua.ithillel.expensetracker.dto.CurrencyRatesDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyConverterImpl implements CurrencyConverter {
    private static final String CURRENCIES_AVAILABLE_API_URL =
            "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies.min.json";
    private static final String CURRENCY_RATES_API_URL =
            "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/%s.min.json";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CurrencyConverterImpl(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public CurrenciesAvailableDTO getCurrenciesAvailable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CURRENCIES_AVAILABLE_API_URL))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), CurrenciesAvailableDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new CurrencyConverterException("Fault while fetching data from API" + e.getMessage());
        }
    }

    @Override
    public CurrencyRatesDTO getCurrencyRates(String currency) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(CURRENCY_RATES_API_URL, currency)))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), CurrencyRatesDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new CurrencyConverterException("Fault while fetching data from API" + e.getMessage());
        }
    }

    public String getCurrenciesAvailableApiUrl() {
        return CURRENCIES_AVAILABLE_API_URL;
    }

    public String getCurrencyRatesApiUrl() {
        return CURRENCY_RATES_API_URL;
    }
}
