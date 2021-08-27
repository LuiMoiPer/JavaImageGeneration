package ImageGeneration.Neighbors.Providers;

import java.util.Arrays;

import ImageGeneration.Point;

public class OscillatingNeighborProvider implements NeighborProvider {

    private Point[] neighbors;
    private int endIndex = 1;
    private boolean expanding = true;

    public OscillatingNeighborProvider(Point[] neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public Point[] getNeighbors() {
        Point[] output = Arrays.copyOfRange(neighbors, 0, endIndex);
        if (expanding && endIndex < neighbors.length) {
            endIndex++;
        }
        else if (expanding && endIndex >= neighbors.length) {
            expanding = false;
            endIndex--;
        }
        else if (!expanding && endIndex > 1) {
            endIndex--;
        }
        else if (!expanding && endIndex <= 1) {
            expanding = true;
            endIndex++;
        }

        return output;
    }
}
