package ImageGeneration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import twitter4j.Status;

public class ImageAndStatus {
    private static final String TEMP_PATH = "./Temp/";
    private static final String IMAGE_NAME = "trendingImage";
    private final BufferedImage image;
    private final Status status;

    private static BufferedImage downloadFromUrl(URL url) {
        String fileExtension;
        String urlString = url.toString();
        int i = urlString.lastIndexOf('.');
        fileExtension = urlString.substring(i + 1);
        BufferedImage image = null;
        
        try {
            String path = TEMP_PATH + IMAGE_NAME + "." + fileExtension; 
            ImageIO.write(
                ImageIO.read(url), 
                fileExtension, 
                new File(path)
            );
            image = ImageIO.read(new File(path));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return image;
    }

    public ImageAndStatus(URL url, Status status) {
        this(downloadFromUrl(url), status);
    }

    public ImageAndStatus(BufferedImage image, Status status) {
        this.image = image;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public BufferedImage getImage() {
        return image;
    }
}
