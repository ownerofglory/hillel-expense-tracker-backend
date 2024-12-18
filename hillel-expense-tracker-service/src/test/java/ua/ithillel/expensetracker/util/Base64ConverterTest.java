package ua.ithillel.expensetracker.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class Base64ConverterTest {
    private final Base64Converter base64Converter = new Base64Converter();

    @Test
    void encodeToStringTest_returnsString() throws IOException {
        String testString = "testString\n";
        String expectedResult = "dGVzdFN0cmluZwo=";
        InputStream in = new ByteArrayInputStream(testString.getBytes());

        String result = base64Converter.encodeToString(in);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedResult, result);
    }

    @Test
    void encodeToStringTest_throwsException() {
        String testString = "testString\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes()) {
            @SneakyThrows
            @Override
            public int read(byte[] b, int off, int len) {
                throw new IOException();
            }
        };

        assertThrows(IOException.class, () -> base64Converter.encodeToString(in));
    }

    @Test
    void encodeToBytesTest_returnsArray() throws IOException {
        String testString = "testString\n";
        String expectedResult = "dGVzdFN0cmluZwo=";
        InputStream in = new ByteArrayInputStream(testString.getBytes());

        byte[] bytes = base64Converter.encodeToBytes(in);

        assertNotNull(bytes);
        assertNotEquals(0, bytes.length);
        assertArrayEquals(expectedResult.getBytes(), bytes);
    }

    @Test
    void encodeToBytesTest_throwsException() {
        String testString = "testString\n";
        InputStream in = new ByteArrayInputStream(testString.getBytes()) {
            @SneakyThrows
            @Override
            public int read(byte[] b, int off, int len) {
                throw new IOException();
            }
        };

        assertThrows(IOException.class, () -> base64Converter.encodeToBytes(in));
    }
}
