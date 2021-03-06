package ImageGeneration;

import java.awt.Color;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

public class Utils {
    public static <T> void shuffleArray(T[] array) {
        Random random = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            T temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static boolean inBounds(Point point, int width, int height) {
        if ((point.getX() >= 0 && point.getX() < width)
            && (point.getY() >= 0 && point.getY() < height)
        ) {
            return true;
        }
        return false;
    }

    public static double distanceFromColor(Color color, Color target) {
        double dist = Math.sqrt(
            Math.pow(target.getRed() - color.getRed(), 2)
            + Math.pow(target.getGreen() - color.getGreen(), 2)
            + Math.pow(target.getBlue() - color.getBlue(), 2)
        );
        return dist;
    }

    public static String timestamp() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SS").format(new Date());
    }

    public static Color randomColor() {
        Random random = ThreadLocalRandom.current();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public static PriorityQueue<Color> randomHeap() {
        return new PriorityQueue<>(16, new ColorByDistance(randomColor()));
    }

    public static List<PriorityQueue<Color>> getEmptyColorProviders(int count) {
        List<PriorityQueue<Color>> colorProviders = new LinkedList<PriorityQueue<Color>>();
        for (int i = 0; i < count; i++) {
            colorProviders.add(randomHeap());
        }
        return colorProviders;
    }

    public static Point randomPoint(int x, int y) {
        Random random = ThreadLocalRandom.current();
        return new Point(random.nextInt(x), random.nextInt(y));
    }

    public static Font randomFont() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = env.getAllFonts();
        Random random = ThreadLocalRandom.current();
        return fonts[random.nextInt(fonts.length)];
    }

    public static void saveImage(BufferedImage image, String path, int millsToSleep) {
        try {
            ImageIO.write(image, "png", new File(path + Utils.timestamp() + ".png"));
        } catch (IOException exception) {
            System.out.println("Could not read file: " + path);
            System.exit(2);
        }
    }
}
