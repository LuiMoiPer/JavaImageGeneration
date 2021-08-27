package ImageGeneration.Neighbors.Providers;

import ImageGeneration.Point;

public class AlternatingNeighborProvider implements NeighborProvider{

    private Point[] initialNeighbors;
    private Point[] alternateNeighbors;
    private boolean inital = true;

    public AlternatingNeighborProvider(Point[] inital, Point[] alternate) {
        this.initialNeighbors = inital;
        this.alternateNeighbors = alternate;
    }

    @Override
    public Point[] getNeighbors() {
        if (inital) {
            inital = !inital;
            return initialNeighbors;
        }
        else {
            inital = !inital;
            return alternateNeighbors;
        }
    }
}
