package ImageGeneration.Searchers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import ImageGeneration.Point;
import ImageGeneration.UniqueDeque;

public class SearcherFactory {
    
    public enum Type {
        ALTERNATING,
        BREADTH_FIRST,
        DEPTH_FIRST
    }

    private BufferedImage image;
    private Set<Point> visited;
    private List<PriorityQueue<Color>> colorProviders;
    private int currColorProvider = 0;
    private List<Point[]> possibleNeighbors;
    private boolean shuffleNeighbors;
    
    public SearcherFactory (
        BufferedImage image,
        Set<Point> visited,
        List<PriorityQueue<Color>> colorProviders,
        List<Point[]> possibleNeighbors,
        boolean shuffleNeighbors
    ) {
        if (colorProviders.size() < 1) {
            throw new IllegalArgumentException("There must be at least one color provider");
        }
        if (possibleNeighbors.size() < 1) {
            throw new IllegalArgumentException("There must be at least one set of neighbors");
        }

        this.image = image;
        this.visited = visited;
        this.colorProviders = colorProviders;
        this.possibleNeighbors = possibleNeighbors;
        this.shuffleNeighbors = shuffleNeighbors;
    }

    public AlternatingSearcher makeAlternatingSearcher(int alternationPeriod) {
        return makeAlternatingSearcher(alternationPeriod, alternationPeriod);
    }

    public AlternatingSearcher makeAlternatingSearcher(int alternationPeriod, Point[] neighbors) {
        return makeAlternatingSearcher(alternationPeriod, alternationPeriod, neighbors);
    }

    public AlternatingSearcher makeAlternatingSearcher(int bfsSteps, int dfsSteps) {
        return new AlternatingSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            getNeighbors(), 
            shuffleNeighbors, 
            bfsSteps, 
            dfsSteps
        );
    }

    public AlternatingSearcher makeAlternatingSearcher(int bfsSteps, int dfsSteps, Point[] neighbors) {
        return new AlternatingSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            neighbors, 
            shuffleNeighbors, 
            bfsSteps, 
            dfsSteps
        );
    }

    public BfSearcher makeBfSearcher() {
        return new BfSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            getNeighbors(), 
            shuffleNeighbors
        );
    }

    public BfSearcher makeBfSearcher(Point[] neighbors) {
        return new BfSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            neighbors, 
            shuffleNeighbors
        );
    }

    public DfSearcher makeDfSearcher() {
        return new DfSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            getNeighbors(), 
            shuffleNeighbors
        );
    }

    public DfSearcher makeDfSearcher(Point[] neighbors) {
        return new DfSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            neighbors, 
            shuffleNeighbors
        );
    }

    private PriorityQueue<Color> getColorProvider() {
        PriorityQueue<Color> colorProvider = colorProviders.get(currColorProvider);
        if (++currColorProvider >= colorProviders.size()) {
            currColorProvider = 0;
        }
        return colorProvider;
    }

    private Point[] getNeighbors() {
        Random random = new Random();
        return possibleNeighbors.get(random.nextInt(possibleNeighbors.size()));
    }
}
