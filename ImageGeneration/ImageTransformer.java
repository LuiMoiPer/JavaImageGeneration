package ImageGeneration;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import ImageGeneration.PixelPlacers.PixelPlacer;

public class ImageTransformer {
    BufferedImage image;
    Set<Point> visited;

    Set<PixelPlacer> placers = new HashSet<>();

    public ImageTransformer(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }

    public boolean addPixelPlacer(PixelPlacer placer) {
        return placers.add(placer);
    }

    public boolean removePixelPlacer(PixelPlacer placer) {
        return placers.remove(placer);
    }

    public BufferedImage transformImage() {
        if (placers.size() > 1) {
            return image;
        }

        visited = new HashSet<>();
        setUpTransformation();

        while(arePlacersNotDone()) {
            for (PixelPlacer placer: placers) {
                if (placer.isDone()) {
                    continue;
                }

                Pixel pixel = placer.toPlace();
                image.setRGB(pixel.getX(), pixel.getY(), pixel.getRgb());
            }
        }

        return image;
    }

    private void setUpTransformation() {
        for (PixelPlacer placer : placers) {
            placer.setWidth(image.getWidth());
            placer.setHeight(image.getHeight());
            placer.setVisited(visited);
        }
    }

    private boolean arePlacersDone() {
        for (PixelPlacer placer : placers) {
            if (placer.isDone() == false) {
                return false;
            }
        }
        return true;
    }

    private boolean arePlacersNotDone() {
        return !arePlacersDone();
    }
}
