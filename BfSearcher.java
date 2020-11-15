import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;
import java.util.Set;

public class BfSearcher extends Searcher {
    public BfSearcher(
        BufferedImage image,
        Set<Point> visited,
        UniqueDeque<Point> frontier,
        PriorityQueue<Color> colorProvider,
        Point[] neighbors,
        boolean shuffleNeighbors
    ) {
        super(image, visited, frontier, colorProvider, neighbors, shuffleNeighbors);
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
