package ImageGeneration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ImageGeneration.ColorProviders.IncreasingRgb;
import ImageGeneration.Neighbors.NeighborBuilder;
import ImageGeneration.Neighbors.Providers.FixedNeighborProvider;
import ImageGeneration.PixelPlacers.BfsPlacer;

public class App {
    private static String inputPath = "./Inputs/banner1.jpg";
    private static String outputPath = "./Outputs/";
    private static BufferedImage image;
    private static int MAX_SEARCHERS = 3;

    public static void main(String[] args) {
        image = loadImage(inputPath);
        BufferedImage transformedImage = refactorTest(image);
        saveImage(transformedImage, outputPath, 0);
    }
    
    static BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        }
        catch (IOException exception) {
            System.out.println("Could not read file: " + path);
            System.exit(1);
        }

        return image;
    }

    static BufferedImage refactorTest(BufferedImage image) {
        ImageTransformer transformer = new ImageTransformer(image);
        transformer.addPixelPlacer(
            new BfsPlacer(new IncreasingRgb(), new FixedNeighborProvider(NeighborBuilder.cardinal()))
        );
        
        return transformer.transformImage();
    }

    static void saveImage(BufferedImage image, String path, int millsToSleep) {
        try {
            ImageIO.write(image, "png", new File(path + Utils.timestamp() + ".png"));
        } catch (IOException exception) {
            System.out.println("Could not read file: " + path);
            System.exit(2);
        }
    }
}
