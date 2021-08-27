package ImageGeneration.Searchers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;
import java.util.Set;

import ImageGeneration.Point;
import ImageGeneration.UniqueDeque;
import ImageGeneration.Neighbors.Providers.NeighborProvider;

public class BfSearcher extends Searcher {
    public BfSearcher(
        BufferedImage image,
        Set<Point> visited,
        UniqueDeque<Point> frontier,
        PriorityQueue<Color> colorProvider,
        NeighborProvider neighborProvider,
        boolean shuffleNeighbors
    ) {
        super(image, visited, frontier, colorProvider, neighborProvider, shuffleNeighbors);
    }

    @Override
    public boolean isDone() {
        if (frontier.isEmpty() || colorProvider.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void seed(Point[] points) {
        for (Point point : points) {
            frontier.add(point);
        }
    }

    @Override
    protected Point nextPoint() {
        return frontier.pollFirst();
    }

    @Override
    protected Color nextColor() {
        return colorProvider.poll();
    }
}
