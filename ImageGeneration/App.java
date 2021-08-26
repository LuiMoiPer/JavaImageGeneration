package ImageGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import javax.imageio.ImageIO;

import ImageGeneration.Neighbors.NeighborFactory;
import ImageGeneration.Searchers.Searcher;
import ImageGeneration.Searchers.SearcherFactory;

public class App {
    private static String inputPath = "./Inputs/banner1.jpg";
    private static String outputPath = "./Outputs/";
    private static BufferedImage image;
    private static int MAX_SEARCHERS = 6;

    public static void main(String[] args) {
        ImageAndStatus imageAndStatus = TwitterDriver.getTrendingImageAndStatus();
        image = imageAndStatus.getImage();
        BufferedImage transformedImage = randomImageTransformation(image);
        saveImage(transformedImage, outputPath, 0);
        TwitterDriver.postImage(image, imageAndStatus.getStatus());
        TwitterDriver.postImageReply(image, imageAndStatus.getStatus());
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

    static void fillHeaps(BufferedImage image, List<PriorityQueue<Color>> heaps) {
        Map<PriorityQueue<Color>, Color> heapToColor = new HashMap<>();
        for (PriorityQueue<Color> heap : heaps) {
            ColorByDistance comparator = (ColorByDistance) heap.comparator();
            heapToColor.put(heap, comparator.getColor());
        }

        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(image.getRGB(x, y));
                // put the color in the best heap
                int bestScore = Integer.MAX_VALUE;
                PriorityQueue<Color> bestHeap = null;
                for (PriorityQueue<Color> heap : heaps) {
                    Color heapColor = heapToColor.get(heap);
                    int score = (int) Utils.distanceFromColor(heapColor, color);
                    if (score < bestScore) {
                        bestScore = score;
                        bestHeap = heap;
                    }
                }
                bestHeap.add(color);
            }
        }
    }

    static BufferedImage randomImageTransformation(BufferedImage image) {
        // choose number of searchers
        Random random = new Random();
        int numSearchers = random.nextInt(MAX_SEARCHERS) + 1;

        // make a group of color providers
        List<PriorityQueue<Color>> colorProviders = Utils.getEmptyColorProviders(numSearchers);
        fillHeaps(image, colorProviders);

        // make the neighbor factory and use it to get some possible neighbors
        NeighborFactory neighborFactory = new NeighborFactory();
        List<Point[]> possibleNeighbors = new LinkedList<>();
        for (int i = 0; i < numSearchers; i++) {
            possibleNeighbors.add(neighborFactory.makeRandomNeighbors());
        }

        // make the searcher factory
        SearcherFactory searcherFactory = new SearcherFactory(
            image, 
            new HashSet<>(), 
            colorProviders, 
            possibleNeighbors, 
            true
        );

        // get searchers from the factory
        Searcher[] searchers = new Searcher[numSearchers];
        for (int i = 0; i < numSearchers; i++) {
            searchers[i] = searcherFactory.makeRandomSearcher();
        }

        // seed the searchers
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        for (Searcher searcher : searchers) {
            Point point = new Point(random.nextInt(imageWidth), random.nextInt(imageHeight));
            searcher.seed(point);
        }

        // run until done
        boolean done = false;
        while (done == false) {
            done = true;
            for (Searcher searcher : searchers) {
                if (searcher.isDone() == false) {
                    done = false;
                    searcher.step();
                }
            }
        }
        return image;
    }
}
