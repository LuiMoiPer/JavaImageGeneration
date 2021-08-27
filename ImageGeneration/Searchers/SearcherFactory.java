package ImageGeneration.Searchers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import ImageGeneration.Point;
import ImageGeneration.UniqueDeque;
import ImageGeneration.Neighbors.Providers.NeighborProvider;

public class SearcherFactory {
    
    public enum Type {
        ALTERNATING,
        BREADTH_FIRST,
        DEPTH_FIRST
    }

    private static int DEFAULT_MIN_BFS_STEPS = 2;
    private static int DEFAULT_MAX_BFS_STEPS = 10;
    private static int DEFAULT_MIN_DFS_STEPS = 2;
    private static int DEFAULT_MAX_DFS_STEPS = 10;

    private BufferedImage image;
    private Set<Point> visited;
    private List<PriorityQueue<Color>> colorProviders;
    private int currColorProvider = 0;
    private List<NeighborProvider> possibleNeighborProviders;
    private boolean shuffleNeighbors;
    
    public SearcherFactory (
        BufferedImage image,
        Set<Point> visited,
        List<PriorityQueue<Color>> colorProviders,
        List<NeighborProvider> possibleNeighborProviders,
        boolean shuffleNeighbors
    ) {
        if (colorProviders.size() < 1) {
            throw new IllegalArgumentException("There must be at least one color provider");
        }
        if (possibleNeighborProviders.size() < 1) {
            throw new IllegalArgumentException("There must be at least one set of neighbors");
        }

        this.image = image;
        this.visited = visited;
        this.colorProviders = colorProviders;
        this.possibleNeighborProviders = possibleNeighborProviders;
        this.shuffleNeighbors = shuffleNeighbors;
    }

    public Searcher makeRandomSearcher() {
        Random random = new Random();
        Type type = Type.values()[random.nextInt(Type.values().length)];
        return makeSearcher(type);
    }

    public Searcher makeSearcher(Type type) {
        switch (type) {
            case ALTERNATING:
                return makeAlternatingSearcher();
            
            case BREADTH_FIRST:
                return makeBfSearcher();

            case DEPTH_FIRST:
                return makeDfSearcher();

            default:
                throw new UnsupportedOperationException("Unsupported searcher type");
        }
    }

    public AlternatingSearcher makeAlternatingSearcher() {
        Random random = new Random();
        int bfsSteps = random.nextInt((DEFAULT_MAX_BFS_STEPS - DEFAULT_MIN_BFS_STEPS)) + DEFAULT_MIN_BFS_STEPS;
        int dfsSteps = random.nextInt((DEFAULT_MAX_DFS_STEPS - DEFAULT_MIN_DFS_STEPS)) + DEFAULT_MIN_DFS_STEPS;
        return makeAlternatingSearcher(bfsSteps, dfsSteps);
    }

    public AlternatingSearcher makeAlternatingSearcher(int alternationPeriod) {
        return makeAlternatingSearcher(alternationPeriod, alternationPeriod);
    }

    public AlternatingSearcher makeAlternatingSearcher(int alternationPeriod, NeighborProvider neighborProvider) {
        return makeAlternatingSearcher(alternationPeriod, alternationPeriod, neighborProvider);
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

    public AlternatingSearcher makeAlternatingSearcher(int bfsSteps, int dfsSteps, NeighborProvider neighborProvider) {
        return new AlternatingSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            neighborProvider, 
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

    public BfSearcher makeBfSearcher(NeighborProvider neighborProvider) {
        return new BfSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            neighborProvider, 
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

    public DfSearcher makeDfSearcher(NeighborProvider neighborProvider) {
        return new DfSearcher(
            image, 
            visited, 
            new UniqueDeque<>(), 
            getColorProvider(), 
            neighborProvider, 
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

    private NeighborProvider getNeighbors() {
        Random random = new Random();
        return possibleNeighborProviders.get(random.nextInt(possibleNeighborProviders.size()));
    }
}
