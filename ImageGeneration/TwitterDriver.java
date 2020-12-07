package ImageGeneration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

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
        String consumerKey = getFileContents(consumerKeyPath);
        String consumerSecret = getFileContents(consumerKeySecretPath);
        String accessToken = getFileContents(accessTokenPath);
        String accessTokenSecret = getFileContents(accessTokenSecretPath);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        twitter = twitterFactory.getInstance();
    }

    private static String getFileContents(String path) {
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

    public static void postImageWithMention() {
        // mention the original user in status
        // upload the image
        // mark explict if original status was explict
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
