package ua.ithillel.expensetracker.util;

import ua.ithillel.expensetracker.exception.ImageConversionException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageConvertor {
    private final static String JPEG_FORMAT = "JPEG";
    private final static float DEFAULT_COMPRESSION_RATE = 0.5f;

    public InputStream convertImageToGrayscale(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage grayScaleImg = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);
            grayScaleImg.getGraphics()
                    .drawImage(image, 0, 0, null);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(grayScaleImg, JPEG_FORMAT, os);

            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new ImageConversionException("Unable to convert the image to grayscale");
        }
    }

    public InputStream compressImage(InputStream inputStream, float compressionRate) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            ImageWriter writer = ImageIO.getImageWritersByFormatName(JPEG_FORMAT).next();
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(compressionRate);

            BufferedImage image = ImageIO.read(inputStream);
            IIOImage iioImage = new IIOImage(image, null, null);
            writer.write(null, iioImage, param);

            byte[] imgData = os.toByteArray();
            writer.dispose();

            return new ByteArrayInputStream(imgData);
        } catch (IOException e) {
            throw new ImageConversionException("Unable to compress the image");
        }
    }

    public InputStream compressImage(InputStream inputStream) {
        return compressImage(inputStream, DEFAULT_COMPRESSION_RATE);
    }
}
