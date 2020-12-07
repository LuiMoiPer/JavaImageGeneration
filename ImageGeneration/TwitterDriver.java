package ImageGeneration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.examples.tweets.UpdateStatus;

public class TwitterDriver {
    private static String consumerKeyPath = "./Candy/ApiKey";
    private static String consumerKeySecretPath = "./Candy/ApiSecretKey";
    private static String accessTokenPath = "./Candy/AccessToken";
    private static String accessTokenSecretPath = "./Candy/AccessTokenSecret";
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
        // check image size
        // if more than 5M then switch to png is possible
        throw new UnsupportedOperationException("Not implemented yet");
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
