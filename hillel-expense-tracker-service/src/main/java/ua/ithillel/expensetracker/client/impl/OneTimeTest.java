package ua.ithillel.expensetracker.client.impl;

import ua.ithillel.expensetracker.dto.CurrenciesAvailableDTO;

import java.io.IOException;

public class OneTimeTest {
    public static void main(String[] args) {
        // Создаем экземпляр вашего класса
        CurrencyConverterImpl currencyConverter = new CurrencyConverterImpl();

        try {
            // Тестируем выполнение метода getCurrenciesAvailable
            System.out.println("===== Тест метода getCurrenciesAvailable =====");
            CurrenciesAvailableDTO currenciesAvailable = currencyConverter.getCurrenciesAvailable();

            // Выводим содержимое объекта currenciesAvailable
            System.out.println(currenciesAvailable);

        } catch (IOException | InterruptedException e) {
            // Обработка ошибок
            System.err.println("Произошла ошибка при вызове getCurrenciesAvailable: " + e.getMessage());
            e.printStackTrace();
        }
    }
}