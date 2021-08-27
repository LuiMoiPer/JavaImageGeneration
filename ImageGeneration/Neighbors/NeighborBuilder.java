package ImageGeneration.Neighbors;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ImageGeneration.Point;
import ImageGeneration.Utils;

/**
 * Used to help build a set of neighbors for search algorithims
 */
public class NeighborBuilder {
    /* instance stuff */
    private Set<Point> neighbors = new HashSet<>();

    public NeighborBuilder() {}

    public NeighborBuilder(Point[] points) {
        neighbors.addAll(Arrays.asList(points));
    }
    
    public boolean add(Point point) {
        return neighbors.add(point);
    }

    public boolean addAll(Point[] points) {
        return this.addAll(Arrays.asList(points));
    }

    public boolean addAll(Collection<Point> points) {
        return neighbors.addAll(points);
    }

    public Point[] getPoints() {
        return neighbors.toArray(new Point[0]);
    }

    public Point[] getShuffledPoints() {
        Point[] points = getPoints();
        Utils.shuffleArray(points);
        return points;
    }

    public int size() {
        return neighbors.size();
    }

    public void scale(int x, int y) {
        this.scale(new Point(x, y));
    }

    public void scale(Point scaleAmount) {
        for (Point point : neighbors) {
            point.scale(point);
        }
    }

    public void  shift(int x, int y) {
        this.shift(new Point(x, y));
    }

    public void shift(Point shiftAmount) {
        for (Point point : neighbors) {
            point.add(shiftAmount);
        }
    }

    /* static stuff */
    public static Point[] cardinal() {
        return new Point[] {
            new Point(1, 0),
            new Point(-1, 0),
            new Point(0, 1),
            new Point(0, -1)
        };
    }

    public static Point[] ordinal() {
        return new Point[] {
            new Point(1, 1),
            new Point(1, -1),
            new Point(-1, 1),
            new Point(-1, -1)
        };
    }

    public static Point[] octo() {
        return new Point[] {
            new Point(1, 1),
            new Point(1, 0),
            new Point(1, -1),
            new Point(0, 1),
            new Point(0, -1),
            new Point(-1, 1),
            new Point(-1, 0),
            new Point(-1, -1)
        };
    }

    public static Point[] knight() {
        return new Point[] {
            new Point(2, 1),
            new Point(2, -1),
            new Point(1, 2),
            new Point(1, -2),
            new Point(-1, 2),
            new Point(-1, -2),
            new Point(-2, 1),
            new Point(-2, -1)
        };
    }

    public static Point[] circle(int radius) {
        List<Point> points = new LinkedList<>();
        Point origin = new Point(0, 0);
        for (int x = 0; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                if (Point.distance(new Point(x, y), origin) <= radius) {
                    points.add(new Point(x, y));
                    points.add(new Point(x, -y));
                    points.add(new Point(-x, y));
                    points.add(new Point(-x, -y));
                }
            }
        }
        return points.toArray(new Point[0]);
    }

    public static Point[] scale(Point[] points, int scaleAmount) {
        for (Point point : points) {
            point.scale(scaleAmount);
        }
        return points;
    }

    public static Point[] scale(Collection<Point> points, int scaleAmount) {
        for (Point point : points) {
            point.scale(scaleAmount);
        }
        return points.toArray(new Point[0]);
    }
}