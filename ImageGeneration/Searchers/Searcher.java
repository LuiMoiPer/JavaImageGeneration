package ImageGeneration.Searchers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import ImageGeneration.Point;
import ImageGeneration.UniqueDeque;
import ImageGeneration.Utils;
import ImageGeneration.Neighbors.Providers.NeighborProvider;

public abstract class Searcher {
    private BufferedImage image;
    private Set<Point> visited;
    private NeighborProvider neighborProvider;
    private boolean shuffleNeighbors;
    protected PriorityQueue<Color> colorProvider;
    protected UniqueDeque<Point> frontier;

    public Searcher() {
        this.image = null;
        this.visited = null;
        this.frontier = null;
        this.colorProvider = null;
        this.neighborProvider = null;
        this.shuffleNeighbors = true;
    }

    public Searcher(
        BufferedImage image,
        Set<Point> visited,
        UniqueDeque<Point> frontier,
        PriorityQueue<Color> colorProvider,
        NeighborProvider neighborProvider,
        boolean shuffleNeighbors
    ) {
        this.image = image;
        this.visited = visited;
        this.frontier = frontier;
        this.colorProvider = colorProvider;
        this.neighborProvider = neighborProvider;
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
        Point[] neighbors = neighborProvider.getNeighbors();

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

    public void seed(Point point) {
        frontier.addFirst(point);
    }

    public void seedText(Font font, String text, boolean addFirst, boolean shufflePoints) {
        // make new image and graphics
        BufferedImage mask = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mask.createGraphics();
        // draw text
        font = new Font(font.getName(), Font.PLAIN, 300);
        graphics.setFont(font);
        graphics.setPaint(Color.WHITE);
        graphics.drawString(text, mask.getWidth() / 3, (mask.getHeight() / 3) * 2);
        Utils.saveImage(mask, "./Outputs/", 0);
        // read the pixels like a mask
        if (addFirst) {
            for (Point point : getPointsFromMask(mask, shufflePoints)) {
                frontier.addFirst(point);
            }
        }
        else {
            for (Point point : getPointsFromMask(mask, shufflePoints)) {
                frontier.addLast(point);
            }
        }
    }

    public Point[] getPointsFromMask(BufferedImage mask, boolean shufflePoints) {
        Set<Point> visiblePoints = new HashSet<>();
        for (int x = 0; x < mask.getWidth(); x++) {
            for (int y = 0; y < mask.getHeight(); y++) {
                if (mask.getRGB(x, y) == Color.WHITE.getRGB()) {
                    visiblePoints.add(new Point(x, y));
                }
            }
        }
        Point[] points = visiblePoints.toArray(new Point[0]);
        if (shufflePoints) {
            Utils.shuffleArray(points);
        }
        return points;
    }

    /* abstract stuff */
    public abstract boolean isDone();
    public abstract void seed(Point[] points);
    protected abstract Point nextPoint();
    protected abstract Color nextColor();
}