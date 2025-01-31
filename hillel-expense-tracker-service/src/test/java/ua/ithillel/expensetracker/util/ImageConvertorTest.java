package ua.ithillel.expensetracker.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ImageConvertorTest {
    private ImageConvertor imageConvertor;

    @BeforeEach
    void setUp() {
        imageConvertor = new ImageConvertor();
    }

    @Test
    void convertImageToGrayscaleTest() {
        InputStream testImage = getTestImage("test-img.jpg");

        InputStream inputStream = imageConvertor.convertImageToGrayscale(testImage);
        assertNotNull(inputStream);
    }

    @Test
    void compressImageTest_noCompressionRate() {
        InputStream testImage = getTestImage("test-img.jpg");

        InputStream inputStream = imageConvertor.compressImage(testImage);
        assertNotNull(inputStream);
    }

    @Test
    void compressImageTest_withCompressionRate() throws IOException {
        InputStream testImage = getTestImage("test-img.jpg");
        int available = testImage.available();

        InputStream inputStream = imageConvertor.compressImage(testImage, 0.5f);
        assertNotNull(inputStream);
    }

    private InputStream getTestImage(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();

        return classLoader.getResourceAsStream(fileName);
    }
}
