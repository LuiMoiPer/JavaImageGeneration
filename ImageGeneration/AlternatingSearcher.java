package ImageGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;
import java.util.Set;

public class AlternatingSearcher extends Searcher {

    private int bfsSteps;
    private int dfsSteps;
    private int stepsTillSwitch;
    private boolean bfs;

    public AlternatingSearcher(
        BufferedImage image,
        Set<Point> visited,
        UniqueDeque<Point> frontier,
        PriorityQueue<Color> colorProvider,
        Point[] neighbors,
        boolean shuffleNeighbors,
        int bfsSteps,
        int dfsSteps
    ) {
        super(image, visited, frontier, colorProvider, neighbors, shuffleNeighbors);
        this.bfsSteps = bfsSteps;
        this.dfsSteps = dfsSteps;
        this.bfs = true;
        this.stepsTillSwitch = bfsSteps;
    }

    public AlternatingSearcher(
        BufferedImage image,
        Set<Point> visited,
        UniqueDeque<Point> frontier,
        PriorityQueue<Color> colorProvider,
        Point[] neighbors,
        boolean shuffleNeighbors,
        int steps
    ) {
        this(image, visited, frontier, colorProvider, neighbors, shuffleNeighbors, steps, steps);
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
            frontier.addFirst(point);
        }
    }

    @Override
    protected Point nextPoint() {
        // do the step
        Point point;
        if (bfs) {
            point = frontier.pollFirst();
        }
        else {
            point = frontier.pollLast();
        }
        // update control mechanism
        if (--stepsTillSwitch == 0) {
            if (bfs == true) {
                stepsTillSwitch = dfsSteps;
            }
            else {
                stepsTillSwitch = bfsSteps;
            }
            bfs = !bfs;
        }
        return point;
    }

    @Override
    protected Color nextColor() {
        return colorProvider.poll();
    }   
}
