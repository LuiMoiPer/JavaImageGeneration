package ImageGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public class App {
    private static String inputPath = "./Inputs/4.jpg";
    private static String outputPath = "./Outputs/";
    private static BufferedImage image;

    public static void main(String[] args) {
        try {
            File imageFile = new File("./Outputs/sizeTest.png");
            System.out.println(Files.size(imageFile.toPath()));
            StatusUpdate statusUpdate = new StatusUpdate("Testing new architechture");
            //statusUpdate.setMediaIds(mediaIds);
            statusUpdate.setMedia(imageFile);
            TwitterDriver.getTwitterInstance().updateStatus(statusUpdate);
        }
        catch (TwitterException exception) {
            System.out.println("=== Twitter Exception ===");
            System.out.println(exception.getMessage());
        }
        catch (Exception Exception) {
            System.out.println("=== Exception ===");
            System.out.println(Exception.getMessage());
        }
    }

    static void bfs(BufferedImage image) {
        // set up frontier
        UniqueDeque<Point> frontier = new UniqueDeque<>();
        // set up heaps
        PriorityQueue<Color> heap = new PriorityQueue<>(16, new ColorByDistance(Utils.randomColor()));
        fillHeap(image, heap);
        // set up neighbors
        Point[] neighbors = NeighborBuilder.knight();
        Utils.shuffleArray(neighbors);
        // set up and run bfs
        AlternatingSearcher as = new AlternatingSearcher(image, 
            new HashSet<>(), 
            frontier,
            heap,
            neighbors,
            true,
            5,
            3
        );
        as.seedText(Utils.randomFont(), "#", true, true);
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

    static BufferedImage randomImageTransformation() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
