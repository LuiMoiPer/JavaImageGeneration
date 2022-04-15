package ImageGeneration;

import java.awt.Color;

public class Pixel {
    private Color color;
    private Point point;

    public Pixel (int x, int y, Color color) {
        this.color = color;
        this.point = new Point(x, y);
    }

    public Pixel (Point point, Color color) {
        this.color = color;
        this.point = point;
    }

    public int getRgb() {
        return color.getRGB();
    }

    public Color getColor() {
        return color;
    }

    public Point getPoint() {
        return point;
    }

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }
}
