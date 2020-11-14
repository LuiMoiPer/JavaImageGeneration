import java.awt.Color;
import java.util.Comparator;

public class ColorByDistance implements Comparator<Color> {
    private Color color;

    public ColorByDistance(Color color) {
        this.color = color;
    }

    @Override
    public int compare(Color a, Color b) {
        double aDistance = Utils.distanceFromColor(color, a);
        double bDistance = Utils.distanceFromColor(color, b);
        return (int) (aDistance - bDistance);
    }
}
