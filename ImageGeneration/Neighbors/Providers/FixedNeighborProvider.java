package ImageGeneration.Neighbors.Providers;

import ImageGeneration.Point;

public class FixedNeighborProvider implements NeighborProvider{

    private Point[] neighbors;

    public FixedNeighborProvider(Point[] neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public Point[] getNeighbors() {
        return neighbors;
    }
}
