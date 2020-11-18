package ImageGeneration.Fonts;

import java.util.HashMap;

import ImageGeneration.Point;

/**
 * https://www.1001fonts.com/bpdots-font.html#character-map
 */
public class BpDots implements Font {
	/* instance stuff */
	@Override
	public int fontWidth() {
		return fontWidth;
	}

	@Override
	public int fontHeight() {
		return fontHeight;
	}

	@Override
	public Point[] getCharacter(char character) {
		return charmap.get(character);
	}

	@Override
	public Point[] getCharacter(char letter, int size) {
		throw new UnsupportedOperationException();
	}

	/* static stuff */
	private static HashMap<Character, Point[]> charmap = new HashMap<>();
	private final static int fontWidth = 5;
	private final static int fontHeight = 5;

    static {
        // 5 by 5
        charmap.put('A', new Point[]{
			new Point(0, 1),
			new Point(0, 2),
			new Point(0, 3),
			new Point(0, 4),
			new Point(1, 0),
			new Point(1, 2),
			new Point(2, 0),
			new Point(2, 2),
			new Point(3, 0),
			new Point(3, 2),
			new Point(4, 1),
			new Point(4, 2),
			new Point(4, 3),
			new Point(4, 4)
		});
		charmap.put('B', new Point[]{
			new Point(0, 0),
			new Point(0, 1),
			new Point(0, 2),
			new Point(0, 3),
			new Point(0, 4),
			new Point(1, 0),
			new Point(1, 2),
			new Point(1, 4),
			new Point(2, 0),
			new Point(2, 2),
			new Point(2, 4),
			new Point(3, 0),
			new Point(3, 2),
			new Point(3, 4),
			new Point(4, 1),
			new Point(4, 3)
		});
		charmap.put('C', new Point[]{
			new Point(0, 1),
			new Point(0, 2),
			new Point(0, 3),
			new Point(1, 0),
			new Point(1, 4),
			new Point(2, 0),
			new Point(2, 4),
			new Point(3, 0),
			new Point(3, 4),
			new Point(4, 0),
			new Point(4, 4)
        });
	}
}
