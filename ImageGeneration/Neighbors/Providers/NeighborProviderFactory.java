package ImageGeneration.Neighbors.Providers;

import java.util.Random;

import ImageGeneration.Neighbors.NeighborFactory;

public class NeighborProviderFactory {

    public enum Type {
        FIXED,
        ALTERNATING,
        OSCILLATING
    }
    
    private NeighborFactory neighborFactory;

    public NeighborProviderFactory(NeighborFactory neighborFactory) {
        this.neighborFactory = neighborFactory;
    }

    public NeighborProvider makeRandomNeighborProvider() {
        Random random = new Random();
        Type type = Type.values()[random.nextInt(Type.values().length)];
        return makeNeighborProvider(type);
    }

    public NeighborProvider makeNeighborProvider(Type type) {
        switch (type) {
            case FIXED:
                return makeFixedNeighborProvider();

            case ALTERNATING:
                return makeAlternatingNeighborProvider();

            case OSCILLATING:
                return makeOscillatingNeighborProvider();
                
            default:
                throw new UnsupportedOperationException("Unsupported neighbor provider type");
        }
    }

    public NeighborProvider makeFixedNeighborProvider() {
        return new FixedNeighborProvider(neighborFactory.makeRandomNeighbors());
    }

    public NeighborProvider makeAlternatingNeighborProvider() {
        return new AlternatingNeighborProvider(neighborFactory.makeRandomNeighbors(), neighborFactory.makeRandomNeighbors());
    }

    public NeighborProvider makeOscillatingNeighborProvider() {
        return new OscillatingNeighborProvider(neighborFactory.makeRandomNeighbors());
    }
}
