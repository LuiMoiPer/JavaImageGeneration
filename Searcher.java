import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class Searcher {
    private BufferedImage image;
    private Set<Point> visited;
    private Point[] neighbors;
    private boolean shuffleNeighbors;
    protected PriorityQueue<Color> colorProvider;
    protected UniqueDeque<Point> frontier;

    public Searcher(
        BufferedImage image,
        Set<Point> visited,
        UniqueDeque<Point> frontier,
        PriorityQueue<Color> colorProvider,
        Point[] neighbors,
        boolean shuffleNeighbors
    ) {
        this.image = image;
        this.visited = visited;
        this.frontier = frontier;
        this.colorProvider = colorProvider;
        this.neighbors = neighbors;
        this.shuffleNeighbors = shuffleNeighbors;
    }

    public boolean step() {
        boolean pixelSet = false;
        if (isDone() == false) {
            Point point = nextPoint();
            if (visited.contains(point) == false) {
                visited.add(point);
                image.setRGB(point.getX(), point.getY(), nextColor().getRGB());
                pixelSet = true;
                expandFrontier(point);
            }
        }
        return pixelSet;
    }

    protected void expandFrontier(Point point) {
        if (shuffleNeighbors) {
            Utils.shuffleArray(neighbors);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        for (Point neighbor : neighbors) {
            Point newPoint = Point.add(point, neighbor);
            if (Utils.inBounds(newPoint, width, height) && visited.contains(newPoint) == false) {
                frontier.addLast(newPoint);                
            }
        }
    }

    /* abstract stuff */
    public abstract boolean isDone();
    public abstract void seed(Point[] points);
    protected abstract Point nextPoint();
    protected abstract Color nextColor();
}