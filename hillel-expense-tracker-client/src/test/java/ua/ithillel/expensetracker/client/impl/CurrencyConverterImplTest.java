package ua.ithillel.expensetracker.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.ithillel.expensetracker.client.exception.CurrencyConverterException;
import ua.ithillel.expensetracker.dto.CurrenciesAvailableDTO;
import ua.ithillel.expensetracker.dto.CurrencyRatesDTO;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CurrencyConverterImplTest {
    private HttpClient mockHttpClient;
    private CurrencyConverterImpl currencyConverter;

    @BeforeEach
    void setUp() {
        mockHttpClient = mock(HttpClient.class);
        ObjectMapper objectMapper = new ObjectMapper();
        currencyConverter = new CurrencyConverterImpl(mockHttpClient, objectMapper);
    }

    @Test
    void getCurrenciesAvailable_ValidApiResponse_CurrenciesFromDTO() throws Exception {
        String apiResponse = "{\"eur\":\"Euro\",\"uah\":\"Ukrainian Hryvnia\",\"usd\":\"US Dollar\"}";

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(apiResponse);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);

        CurrenciesAvailableDTO result = currencyConverter.getCurrenciesAvailable();

        assertNotNull(result);
        assertEquals("Euro", result.getAvailableCurrencies().get("eur"));
        assertEquals("Ukrainian Hryvnia", result.getAvailableCurrencies().get("uah"));
        assertEquals("US Dollar", result.getAvailableCurrencies().get("usd"));

        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass((HttpRequest.class));
        verify(mockHttpClient).send(captor.capture(), eq(HttpResponse.BodyHandlers.ofString()));
        assertEquals(currencyConverter.getCurrenciesAvailableApiUrl(), captor.getValue().uri().toString());
    }

    @Test
    void getCurrencyRates_ValidApiResponse_RatesFromDTO() throws Exception {
        String currencyCode = "uah";
        String apiResponse = String.format(
                "{\"date\":\"2025-01-11\",\"%s\":{\"eur\":0.023043879,\"usd\":0.023606956,\"btc\":2.4909824e-7}}",
                currencyCode);

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(apiResponse);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);

        CurrencyRatesDTO result = currencyConverter.getCurrencyRates(currencyCode);

        assertNotNull(result);
        assertEquals(0.023043879, result.getRates().get("eur"));
        assertEquals(0.023606956, result.getRates().get("usd"));
        assertEquals(0.00000024909824, result.getRates().get("btc"));

        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass((HttpRequest.class));
        verify(mockHttpClient).send(captor.capture(), eq(HttpResponse.BodyHandlers.ofString()));
        assertEquals(String.format(currencyConverter.getCurrencyRatesApiUrl(), currencyCode),
                captor.getValue().uri().toString());
    }

    @Test
    void getCurrenciesAvailable_Error_ThrowsException() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("API Error"));

        CurrencyConverterException exception = assertThrows(CurrencyConverterException.class,
                () -> currencyConverter.getCurrenciesAvailable());

        assertTrue(exception.getMessage().contains("Fault while fetching data from API"));
    }

    @Test
    void getCurrencyRates_Error_ThrowsException() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("API Error"));

        CurrencyConverterException exception = assertThrows(CurrencyConverterException.class,
                () -> currencyConverter.getCurrencyRates("currencyCode"));

        assertTrue(exception.getMessage().contains("Fault while fetching data from API"));
    }
}
