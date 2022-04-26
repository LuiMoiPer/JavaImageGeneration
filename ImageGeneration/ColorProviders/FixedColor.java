package ImageGeneration.ColorProviders;

import java.awt.Color;

public class FixedColor extends ColorProvider {
    Color color;

    public FixedColor(Color color) {
        this.color = color;
    }

    @Override
    public Color nextColor() {
        return color;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
    
}
