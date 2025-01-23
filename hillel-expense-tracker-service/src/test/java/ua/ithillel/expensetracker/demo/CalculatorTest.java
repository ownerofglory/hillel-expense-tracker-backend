package ua.ithillel.expensetracker.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
    private Calculator calculator;

    @BeforeAll
    static void classSetUp() {
        System.out.println("Class set up");
    }


    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }


    @Test
    void addTest_returnsNumber() {
        int a = 1;
        int b = 2;
        int expected = 3;

        int actualResult = calculator.add(a, b);

        assertEquals(expected, actualResult);
    }

    @Test
    void divideTest_returnsNumber() {
        int a = 10;
        int b = 2;
        int expected = 5;

        int actualResult = calculator.divide(a, b);

        assertEquals(expected, actualResult);
    }

    @Test
    void divideTest_dividerZeroThrowsException() {
        int a = 10;
        int b = 0;

        assertThrows(ArithmeticException.class, () -> calculator.divide(a, b));
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test finished");
        calculator = null;
    }
}
