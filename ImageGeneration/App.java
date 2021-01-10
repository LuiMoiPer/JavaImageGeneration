package ImageGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import ImageGeneration.Searchers.AlternatingSearcher;
import ImageGeneration.Searchers.BfSearcher;
import ImageGeneration.Searchers.Searcher;
import ImageGeneration.Searchers.SearcherFactory;
import ImageGeneration.Searchers.SearcherFactory.Type;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public class App {
    private static String inputPath = "./Temp/trendingImage.jpg";
    private static String outputPath = "./Outputs/";
    private static BufferedImage image;

    public static void main(String[] args) {
        ImageAndStatus imageAndStatus = TwitterDriver.getTrendingImageAndStatus();
        loadImage(inputPath);
        BufferedImage transformedImage = randomImageTransformation(image);
        saveImage(transformedImage, outputPath, 0);
        // TwitterDriver.postImage(image, imageAndStatus.getStatus());
    }

    static void bfs(BufferedImage image) {
        // set up frontier
        UniqueDeque<Point> frontier = new UniqueDeque<>();
        frontier.add(new Point(0, 0));
        // set up heaps
        PriorityQueue<Color> heap = new PriorityQueue<>(16, new ColorByDistance(Utils.randomColor()));
        fillHeap(image, heap);
        // set up neighbors
        Point[] neighbors = NeighborBuilder.knight();
        Utils.shuffleArray(neighbors);
        // set up and run bfs
        BfSearcher bfs = new BfSearcher(image, 
            new HashSet<>(), 
            frontier,
            heap,
            neighbors,
            true
        );
        while (bfs.isDone() == false) {
            bfs.step();
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

        for (PriorityQueue<Color> heap : heaps) {
            System.out.println("Heap size: " + heap.size());
        }
    }

    static BufferedImage randomImageTransformation(BufferedImage image) {
        // choose number of searchers
        Random random = new Random();
        int numSearchers = 5; 
        // make a group of color providers
        List<PriorityQueue<Color>> colorProviders = Utils.getEmptyColorProviders(numSearchers);
        fillHeaps(image, colorProviders);
        // make a list of possible neighbors
        List<Point[]> possibleNeighbors = new LinkedList<>();
        possibleNeighbors.add(NeighborBuilder.carinal());
        // possibleNeighbors.add(NeighborBuilder.knight());
        possibleNeighbors.add(NeighborBuilder.circle(5));
        // make the factory
        SearcherFactory searcherFactory = new SearcherFactory(
            image, 
            new HashSet<>(), 
            colorProviders, 
            possibleNeighbors, 
            true
        );
        // get searchers from the factory
        Searcher[] searchers = new Searcher[numSearchers];
        Type[] types = Type.values();
        for (int i = 0; i < numSearchers; i++) {
            Type type = types[random.nextInt(types.length)];
            searchers[i] = searcherFactory.makeSearcher(type);
        }
        // seed the searchers
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        for (Searcher searcher : searchers) {
            Point point = new Point(random.nextInt(imageWidth), random.nextInt(imageHeight));
            searcher.seed(point);
        }

        for (Searcher searcher : searchers) {
            System.out.println("Searcher is done: " + searcher.isDone());
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
