package ua.ithillel.expensetracker;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        File input = new File("docs/bill_picture.jpg");
        BufferedImage image = ImageIO.read(input);
        BufferedImage grayImage = Scalr.apply(image, Scalr.OP_GRAYSCALE);

        File output = new File("docs/gray_bill_compressed.jpg");
        ImageIO.write(grayImage, "jpg", output);
    }
}
