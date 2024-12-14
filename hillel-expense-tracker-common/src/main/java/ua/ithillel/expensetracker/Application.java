package ua.ithillel.expensetracker;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
//        File input = new File("docs/bill_picture.jpg");
//        BufferedImage image = ImageIO.read(input);
//        BufferedImage grayImage = Scalr.apply(image, Scalr.OP_GRAYSCALE);
//
//        File output = new File("docs/gray_bill_compressed.jpg");
//        ImageIO.write(grayImage, "jpg", output);

        File input = new File("docs/bill_picture.jpg");
        BufferedImage image = ImageIO.read(input);


        BufferedImage grayImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        grayImage.getGraphics().drawImage(image, 0, 0, null);


        File output = new File("docs/gray_bill_compressed2.jpg");
        try (FileOutputStream fos = new FileOutputStream(output);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {

            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(ios);

            // Настройка уровня сжатия
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f); // 0.5 = 50% качества

            writer.write(null, new javax.imageio.IIOImage(grayImage, null, null), param);
            writer.dispose();
        }

    }
}
