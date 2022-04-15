package ImageGeneration;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import ImageGeneration.Neighbors.Providers.NeighborProvider;

public abstract class PixelPlacer {
    private int width;
    private int height;
    protected UniqueDeque<Point> frontier;
    protected Set<Point> visited;
    protected ColorProvider colorProvider;
    protected NeighborProvider neighborProvider;

    public PixelPlacer(ColorProvider colorProvider, NeighborProvider neighborProvider) {
        this(0, 0, new HashSet<>(), colorProvider, neighborProvider);
    }

    public PixelPlacer(Set<Point> visited, ColorProvider colorProvider, NeighborProvider neighborProvider) {
        this(0, 0, visited, colorProvider, neighborProvider);
    }

    public PixelPlacer(Point bounds, Set<Point> visited, ColorProvider colorProvider, NeighborProvider neighborProvider) {
        this(bounds.getX(), bounds.getY(), visited, colorProvider, neighborProvider);
    }

    public PixelPlacer(int width, int height, Set<Point> visited, ColorProvider colorProvider, NeighborProvider neighborProvider) {
        this.frontier = new UniqueDeque<>();
        this.width = width;
        this.height = height;
        this.visited = visited;
        this.colorProvider = colorProvider;
        this.neighborProvider = neighborProvider;
    }
    
    public boolean isDone() {
        return (frontier.size() == 0 || colorProvider.isEmpty());
    }
    
    public Pixel toPlace() {
        while (isDone() == false) {
            Point point = nextPoint();
            if (visited.contains(point) == false) {
                visited.add(point);
                expandFrontier(point);
                return new Pixel(point, nextColor());
            }
        }
        return null;
    }
    
    public boolean isSeeded() {
        return frontier.size() > 0;
    }

    public void setVisited(Set<Point> visited) {
        this.visited = visited;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected void expandFrontier(Point point) {
        Point[] neighbors = neighborProvider.getNeighbors();
        for (Point neighbor: neighbors) {
            Point newPoint = Point.add(point, neighbor);
            if (Utils.inBounds(newPoint, width, height) && visited.contains(newPoint) == false) {
                addToFrontier(newPoint);
            }
        }
    }

    public abstract void seed(Point point);
    protected abstract Point nextPoint();
    protected abstract Color nextColor();
    protected abstract void addToFrontier(Point point);
}
