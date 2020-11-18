package ImageGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

public class App {
    private static String inputPath = "./Inputs/3.jpg";
    private static String outputPath = "./Outputs/";
    private static BufferedImage image;

    public static void main(String[] args) {
        loadImage(inputPath);
        bfs(image);
        saveImage(image, outputPath, 0);
    }

    static void bfs(BufferedImage image) {
        // set up frontier
        UniqueDeque<Point> frontier = new UniqueDeque<>();
        int width = image.getWidth();
        int height = image.getHeight();
        frontier.add(Utils.randomPoint(width, height));
        // set up heaps
        PriorityQueue<Color> heap = new PriorityQueue<>(16, new ColorByDistance(Utils.randomColor()));
        fillHeap(image, heap);
        // set up neighbors
        Point[] neighbors = new Point[]{
            new Point(-1, -1),
            new Point(1, 0),
            new Point(-1, 0),
            new Point(1, 1)
        };
        Utils.shuffleArray(neighbors);
        // set up and run bfs
        AlternatingSearcher as = new AlternatingSearcher(image, 
            new HashSet<>(), 
            frontier,
            heap,
            neighbors,
            true,
            1,
            3
        );
        while (as.isDone() == false) {
            as.step();
        }
    }

    static void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        }
        catch (IOException exception) {
            System.out.println("Could not read file: " + path);
            System.exit(1);
        }
    }

    static void saveImage(BufferedImage image, String path, int millsToSleep) {
        try {
            ImageIO.write(image, "png", new File(path + Utils.timestamp() + ".png"));
        } catch (IOException exception) {
            System.out.println("Could not read file: " + path);
            System.exit(2);
        }
    }

    static void fillHeap(BufferedImage image, PriorityQueue<Color> heap) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                heap.add(color);
            }
        }
    }
}