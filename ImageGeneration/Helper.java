package ImageGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Deque;

public class Helper {
    public static void main(String[] args) {
        App.saveImage(grayscaleTestImage(), "./Inputs/", 0);
    }

    public static BufferedImage grayscaleTestImage() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        int value = 0;
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                image.setRGB(x, y, new Color(value, value, value).getRGB());
                value += 1;
            }
        }
        return image;
    }

    public static BufferedImage frontierImage(Deque<Point> frontier, BufferedImage image) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (Point point : frontier) {
            output.setRGB(point.getX(), point.getY(), Color.RED.getRGB());
        }
        return output;
    }
}
