import java.util.LinkedList;
import java.util.List;
/**
 * Used to help generate neighbors
 */
public class NeighborGenerator {
    /* instance stuff */
    /* static stuff */
    public static Point[] carinal() {
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
}