public class Point {

    /* instance stuff */

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void add(Point point) {
        this.x += point.getX();
        this.y += point.getY();
    }

    public void scale(Point point) {
        this.x *= point.getX();
        this.y *= point.getY();
    }

    public void scale(int scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = hash * 29 + x;
        hash = hash * 29 + y;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // self check
        if (this == object) {
            return true;
        }
        // null check
        if (object == null) {
            return false;
        }
        // type check
        if (getClass() != object.getClass()) {
            return false;
        }
        // field check
        Point other = (Point) object;
        return (x == other.getX() && y == other.getY());
    }

    /* static stuff */
    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }
    
    public static Point scale(Point a, Point b) {
        return new Point(a.x * b.x, a.y * b.y);
    }
    
    public static Point scale(Point point, int scalar) {
        return new Point(point.x * scalar, point.y * scalar);
    }

    public static double distance(Point a, Point b) {
        return Math.sqrt(
            Math.pow((a.x - b.x), 2)
            + Math.pow((a.y - b.y), 2)
        );
    }
    public static void main(String[] args) {
        
    }
}
