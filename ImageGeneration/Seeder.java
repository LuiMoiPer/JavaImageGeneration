package ImageGeneration;

import ImageGeneration.Fonts.*;

/**
 * Used to help seed frontiers so that a pattern appears in the final image
 */
public class Seeder {   
    
    private static Font font = new BpDots();
    private static int charSpacing = 1;

    
    public static void addText(Searcher searcher, String text) {
        NeighborBuilder points = new NeighborBuilder();
        char[] textArray = text.toCharArray();
        for (int i = textArray.length -1; i >= 0; i--) {
            // Shift the points over by character width
            points.shift(font.fontWidth(), 0);
            // Shift the points over for spacing between chars
            points.shift(charSpacing, 0);
            
            points.addall(font.getCharacter(textArray[i]));
        }
        // add character shift over some amount + space
        // set the points in the searcher
    }

    public static void setCharSpacing(int spacing) {
        Seeder.charSpacing = spacing;
    }

    public static void setFont(Font font) {
        Seeder.font = font;
    }
}
