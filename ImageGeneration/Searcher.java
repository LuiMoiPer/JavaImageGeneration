package ImageGeneration;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
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

    //  seedText(Utils.randomFont(), "Boop", true, true)
    public void seedText(Font font, String text, boolean addFirst, boolean shufflePoints) {
        // make new image and graphics
        BufferedImage mask = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mask.createGraphics();
        // draw text
        font = new Font(font.getName(), Font.PLAIN, 600);
        graphics.setFont(font);
        graphics.setPaint(Color.WHITE);
        graphics.drawString(text, mask.getWidth() / 3, (mask.getHeight() / 3) * 2);
        App.saveImage(mask, "./Outputs/", 0);
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