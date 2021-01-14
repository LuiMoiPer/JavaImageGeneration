package ImageGeneration;

import java.util.Random;

public class NeighborFactory {

    private static int MAX_CARDINAL_SCALE = 10;
    private static int MAX_CIRCLE_SCALE = 10;
    private static int MIN_CIRCLE_SCALE = 3;
    private static int MAX_KNIGHT_SCALE = 10;
    private static int MAX_OCTAGONAL_SCALE = 10;
    private static int MAX_ORDINAL_SCALE = 10;
    private static int MAX_PERCENTAGE_SCALE = 10;
    private static int MIN_PERCENTAGE_SCALE = 3;
    private static int MIN_PERCENTAGE_CHANCE = 10;
    private static int MAX_PERCENTAGE_CHANCE = 70;
    private static int MAX_GAP_SIZE = 500;
    private static int MIN_GAP_SIZE = 50;
    
    public enum Type {
        CARDINAL,
        CIRCLE,
        COMBINATION,
        GAPPED,
        KNIGHT,
        OCTAGONAL,
        ORDINAL,
        PERCENTAGE
    }

    private Type[] possibleTypes;

    public NeighborFactory() {
        possibleTypes = Type.values();
    }

    public NeighborFactory(Type[] possibeTypes) {
        this.possibleTypes = possibeTypes;
    }

    public Point[] makeRandomNeighbors() {
        Random random = new Random();
        Type type = possibleTypes[random.nextInt(possibleTypes.length)];
        return makeNeighbors(type);
    }

    public Point[] makeNeighbors(Type type) {
        switch (type) {
            case CARDINAL:
                return makeCardinal();

            case CIRCLE:
                return makeCircle();

            case COMBINATION:
                return makeCombination();

            case GAPPED:
                return makeGapped();

            case KNIGHT:
                return makeKnight();

            case OCTAGONAL:
                return makeOctoganal();

            case ORDINAL:
                return makeOrdinal();

            case PERCENTAGE:
                return makePercentage();
            default:
                throw new UnsupportedOperationException("Unsupported neighbor type");
        }
    }

    public Point[] makePercentage() {
        Random random = new Random();
        int scale = random.nextInt(MAX_PERCENTAGE_SCALE - MIN_PERCENTAGE_SCALE) + MIN_PERCENTAGE_SCALE;
        int chance = random.nextInt(MAX_PERCENTAGE_CHANCE - MIN_PERCENTAGE_CHANCE) + MIN_PERCENTAGE_CHANCE;
        return makePercentage(scale, chance);
    }

    public Point[] makePercentage(int scale, int chance) {
        if (scale < 1) {
            throw new IllegalArgumentException("Scale must be greater than 0.");
        }

        NeighborBuilder neighborBuilder = new NeighborBuilder();
        Random random = new Random();
        for (int x = -scale; x < scale; x++) {
            for (int y = -scale; y < scale; y++) {
                if (random.nextInt(100) < chance) {
                    neighborBuilder.add(new Point(x, y));
                }
            }
        }
        return neighborBuilder.getPoints();
    }

    public Point[] makeOrdinal() {
        Random random = new Random();
        int scale = random.nextInt(MAX_ORDINAL_SCALE) + 1;
        return makeOrdinal(scale);
    }

    public Point[] makeOrdinal(int scale) {
        NeighborBuilder neighborBuilder = new NeighborBuilder(NeighborBuilder.ordinal());
        for (int i = 2; i <= scale; i++) {
            neighborBuilder.addAll(NeighborBuilder.scale(NeighborBuilder.ordinal(), i));
        }
        return neighborBuilder.getPoints();
    }

    public Point[] makeOctoganal() {
        Random random = new Random();
        int scale = random.nextInt(MAX_OCTAGONAL_SCALE) + 1;
        return makeOctoganal(scale);
    }

    public Point[] makeOctoganal(int scale) {
        NeighborBuilder neighborBuilder = new NeighborBuilder(NeighborBuilder.octo());
        for (int i = 2; i <= scale; i++) {
            neighborBuilder.addAll(NeighborBuilder.scale(NeighborBuilder.octo(), i));
        }
        return neighborBuilder.getPoints();
    }

    public Point[] makeKnight() {
        Random random = new Random();
        int scale = random.nextInt(MAX_KNIGHT_SCALE) + 1;
        return makeKnight(scale);
    }

    public Point[] makeKnight(int scale) {
        NeighborBuilder neighborBuilder = new NeighborBuilder(NeighborBuilder.knight());
        for (int i = 2; i <= scale; i++) {
            neighborBuilder.addAll(NeighborBuilder.scale(NeighborBuilder.knight(), i));
        }
        return neighborBuilder.getPoints();
    }

    public Point[] makeGapped() {
        Point[] neighbors = NeighborBuilder.carinal();
        Random random = new Random();
        int gapSize = random.nextInt(MAX_GAP_SIZE - MIN_GAP_SIZE) + MIN_GAP_SIZE;
        return makeGapped(neighbors, gapSize);
    }

    public Point[] makeGapped(Point[] neighbors, int gapSize) {
        NeighborBuilder neighborBuilder = new NeighborBuilder(neighbors);
        
        for (Point point : neighborBuilder.getPoints()) {
            int x = point.getX();
            int y = point.getY();
            neighborBuilder.add(new Point(x + gapSize, y));
            neighborBuilder.add(new Point(x - gapSize, y));
            neighborBuilder.add(new Point(x, y + gapSize));
            neighborBuilder.add(new Point(x, y - gapSize));
        }
        
        return neighborBuilder.getPoints();
    }

    public Point[] makeCombination() {
        NeighborBuilder neighborBuilder = new NeighborBuilder();
        neighborBuilder.addAll(makeRandomNeighbors());
        neighborBuilder.addAll(makeRandomNeighbors());
        return neighborBuilder.getPoints();
    }

    public Point[] makeCircle() {
        Random random = new Random();
        int scale = random.nextInt(MAX_CIRCLE_SCALE - MIN_CIRCLE_SCALE) + MIN_CIRCLE_SCALE;
        return NeighborBuilder.circle(scale);
    }

    public Point[] makeCircle(int scale) {
        return NeighborBuilder.circle(scale);
    }

    public Point[] makeCardinal() {
        Random random = new Random();
        int scale = random.nextInt(MAX_CARDINAL_SCALE) + 1;
        return makeCardinal(scale);
    }

    public Point[] makeCardinal(int scale) {
        NeighborBuilder neighborBuilder = new NeighborBuilder(NeighborBuilder.carinal());
        for (int i = 2; i <= scale; i++) {
            neighborBuilder.addAll(NeighborBuilder.scale(NeighborBuilder.carinal(), i));
        }
        return neighborBuilder.getPoints();
    }
}
