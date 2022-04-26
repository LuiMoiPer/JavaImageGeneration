package ImageGeneration.PixelPlacers;

import ImageGeneration.Point;
import ImageGeneration.ColorProviders.ColorProvider;
import ImageGeneration.Neighbors.Providers.NeighborProvider;

public class BfsPlacer extends PixelPlacer {

    public BfsPlacer(ColorProvider colorProvider, NeighborProvider neighborProvider) {
        super(colorProvider, neighborProvider);
    }

    @Override
    public void seed(Point point) {
        frontier.add(point);
    }

    @Override
    protected Point nextPoint() {
        return frontier.getFirst();
    }

    @Override
    protected void addToFrontier(Point point) {
        frontier.addLast(point);
    }
}
