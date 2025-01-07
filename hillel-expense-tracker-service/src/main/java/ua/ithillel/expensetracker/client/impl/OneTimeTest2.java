package ua.ithillel.expensetracker.client.impl;

import ua.ithillel.expensetracker.dto.CurrencyRatesDTO;

import java.io.IOException;

public class OneTimeTest2 {
    public static void main(String[] args) {
        // Создаем экземпляр вашего класса
        CurrencyConverterImpl currencyConverter = new CurrencyConverterImpl();

            // Базовая валюта для тестирования
            String baseCurrency = "usd";

            // Тестируем выполнение метода getCurrencyRates
            System.out.println("===== Тест метода getCurrencyRates =====");
            CurrencyRatesDTO currencyRates = currencyConverter.getCurrencyRates(baseCurrency);

            // Выводим содержимое объекта CurrencyRatesDTO
            System.out.println("Курсы валют относительно: " + baseCurrency.toUpperCase());
            System.out.println(currencyRates);


    }
}