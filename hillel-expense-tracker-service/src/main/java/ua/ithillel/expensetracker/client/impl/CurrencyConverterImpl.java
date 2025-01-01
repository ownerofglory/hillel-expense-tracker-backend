package ua.ithillel.expensetracker.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.ithillel.expensetracker.client.CurrencyConverter;
import ua.ithillel.expensetracker.dto.CurrenciesAvailableDTO;
import ua.ithillel.expensetracker.dto.CurrencyRatesDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyConverterImpl implements CurrencyConverter {
    @Override
    public CurrenciesAvailableDTO getCurrenciesAvailable() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies.min.json"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.body(), CurrenciesAvailableDTO.class);
    }

    @Override
    public CurrencyRatesDTO getCurrencyRates(String currency) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/" + currency + ".min.json"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.body(), CurrencyRatesDTO.class);
    }
}
