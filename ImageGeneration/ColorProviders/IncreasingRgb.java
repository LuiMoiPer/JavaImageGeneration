package ImageGeneration.ColorProviders;

import java.awt.Color;

public class IncreasingRgb extends ColorProvider {
    int currentColor;

    public IncreasingRgb() {
        this.currentColor = 0;
    }

    public IncreasingRgb(int startColor) {
        this.currentColor = startColor;
    }

    public IncreasingRgb(Color startColor) {
        this.currentColor = startColor.getRGB();
    }

    @Override
    public Color nextColor() {
        currentColor++;
        return new Color(currentColor);
    }

    @Override
    public boolean isEmpty() {
        return currentColor == Integer.MAX_VALUE;
    }
}
