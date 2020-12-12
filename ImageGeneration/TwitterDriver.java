package ImageGeneration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import javax.imageio.ImageIO;

import twitter4j.HttpParameter;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterDriver {
    private static String consumerKeyPath = "./Candy/ApiKey";
    private static String consumerKeySecretPath = "./Candy/ApiSecretKey";
    private static String accessTokenPath = "./Candy/AccessToken";
    private static String accessTokenSecretPath = "./Candy/AccessTokenSecret";
    private static Twitter twitter;

    private final static String TEMP_PATH = "./temp";
    private final static String RESIZE_PATH = "./tempResize";
    private static File tempImg = new File(TEMP_PATH + ".png");
    private static File tempResizedImg = new File(RESIZE_PATH + ".png");
    private static long MAX_UPLOAD_SIZE = 5242880;

    static {
        setupTwitterInstance();
        try {
            tempImg.createNewFile();
            tempResizedImg.createNewFile();
        }
        catch (Exception exception) {
            System.out.println("Exception happened in creating file");
        }
    }

    private TwitterDriver() {}

    private static void cleanupTempFiles() {
        tempImg.delete();
        tempResizedImg.delete();
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

    private static File resizeImage(BufferedImage image) {
        // while resized image size is larger than max upload size
        //   attempt resize
        // return resized file
        throw new UnsupportedOperationException("Not yet implemented");
    }

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
        try {
            ImageIO.write(image, "png", tempImg);
            System.out.println("PNG size: " + Files.size(tempImg.toPath()));
            // if the png is less than max upload return that
            if (Files.size(tempImg.toPath()) < MAX_UPLOAD_SIZE) {
                System.out.println("Setting output to png");
                output = tempImg;
            }
            // else resize
            else {
                System.out.println("Resizing image ");
                output = resizeImage(image);
            }
        } 
        catch (IOException exception) {
            System.out.println("=== IO Exception ===");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return output;
    }

    public static Status getStatusWithImage() {
        // search twitter for a status that also has an image
        Query query = new Query("art");
        query.setCount(20);
        query.setResultType(Query.POPULAR);
        System.out.println(query.getQuery());
        QueryResult result = null;
        try {
            result = twitter.search(query);
        }
        catch (TwitterException exception) {
            System.out.println("=== Twitter Exception ===");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        for (Status status : result.getTweets()) {
            System.out.println(status.getText());
            System.out.println(status.getId());
            MediaEntity[] mediaEntities = status.getMediaEntities();
            for (MediaEntity mediaEntity : mediaEntities) {
                System.out.println("Media of type: " + mediaEntity.getType());
            }
            //System.out.print("+");
        }

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