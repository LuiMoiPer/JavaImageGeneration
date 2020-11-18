package ImageGeneration.Fonts;

import ImageGeneration.Point;

public interface Font {
    public int fontWidth();
    public int fontHeight();
    public Point[] getCharacter(char character);
    public Point[] getCharacter(char character, int size);
}
