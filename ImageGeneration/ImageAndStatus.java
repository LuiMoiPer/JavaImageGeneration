package ImageGeneration;

import java.awt.image.BufferedImage;
import java.net.URL;

import twitter4j.Status;

public class ImageAndStatus {

    private final BufferedImage image;
    private final Status status;

    public ImageAndStatus(URL url, Status status) {
        // download the image from the url
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ImageAndStatus(BufferedImage image, Status status) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Status getStatus() {
        return status;
    }

    public BufferedImage getImage() {
        return image;
    }
}
