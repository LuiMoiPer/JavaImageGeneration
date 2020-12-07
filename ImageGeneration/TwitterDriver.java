package ImageGeneration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import javax.imageio.ImageIO;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterDriver {
    private static String consumerKeyPath = "./Candy/ApiKey";
    private static String consumerKeySecretPath = "./Candy/ApiSecretKey";
    private static String accessTokenPath = "./Candy/AccessToken";
    private static String accessTokenSecretPath = "./Candy/AccessTokenSecret";
    private static String tempPath = "./temp";
    private static int MAX_UPLOAD_SIZE;
    private static Twitter twitter;
    static {
        setupTwitterInstance();
    }

    private TwitterDriver() {}

    private static void setupTwitterInstance() {
        String consumerKey = getAuthFileContents(consumerKeyPath);
        String consumerSecret = getAuthFileContents(consumerKeySecretPath);
        String accessToken = getAuthFileContents(accessTokenPath);
        String accessTokenSecret = getAuthFileContents(accessTokenSecretPath);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        twitter = twitterFactory.getInstance();
    }

    private static String getAuthFileContents(String path) {
        String contents = "";
        try {
            Scanner fileReader = new Scanner(new File(path));
            contents = fileReader.nextLine();
        }
        catch (FileNotFoundException exception) {
            System.out.println("File not found: " + path);
        }
        return contents;
    }

    private static StatusUpdate setupStatusUpdate(BufferedImage image, Status originalStatus) {
        StatusUpdate statusUpdate = new StatusUpdate("Rearranged the pixels");
        // attach the image
        statusUpdate.setMedia(getUploadableImage(image));
        // mark explict if original status was explict
        statusUpdate.setPossiblySensitive(originalStatus.isPossiblySensitive());
        return statusUpdate;
    }

    public static Twitter getTwitterInstance() {
        if (twitter == null) {
            setupTwitterInstance();
        }
        return twitter;
    }

    public static File getUploadableImage(BufferedImage image) {
        // save image as both png and jpg
        File output = null;
        File png = new File(tempPath + ".png");
        File jpg = new File(tempPath + ".jpg");
        try {
            ImageIO.write(image, "png", png);
            ImageIO.write(image, "jpg", jpg);
            // if the png is less than max upload return that
            if (Files.size(png.toPath()) > MAX_UPLOAD_SIZE) {
                output = png;
            }
            // else if jpg is less than max upload return that
            else if (Files.size(jpg.toPath()) > MAX_UPLOAD_SIZE) {
                output = jpg;
            }
            // else resize
            else {
                throw new UnsupportedOperationException("Resize not yet implemented");
            }
        } 
        catch (IOException exception) {
            System.out.println("=== IO Exception ===");
            System.out.println(exception.getMessage());
        }
        return output;
    }

    public static Status getStatusWithImage() {
        // search twitter for a status that also has an image
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static void postImage(BufferedImage image, Status originalStatus) {
        StatusUpdate statusUpdate = setupStatusUpdate(image, originalStatus);
        // Post it
        try {
            twitter.updateStatus(statusUpdate);
        }
        catch (TwitterException exception) {
            System.out.println("=== Twitter Exception ===");
            System.out.println(exception.getMessage());
        }
    }

    public static void postImageReply(BufferedImage image, Status originalStatus) {
        StatusUpdate statusUpdate = setupStatusUpdate(image, originalStatus);
        // Set update as a reply to the original status
        statusUpdate.setInReplyToStatusId(originalStatus.getId());
        // Post it
        try {
            twitter.updateStatus(statusUpdate);
        }
        catch (TwitterException exception) {
            System.out.println("=== Twitter Exception ===");
            System.out.println(exception.getMessage());
        }
    }
}