package ImageGeneration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Trend;
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

    private final static String TEMP_PATH = "./Temp/temp";
    private final static String RESIZE_PATH = "./Temp/tempResize";
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

    private TwitterDriver() {
    }

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

    private static ImageAndStatus getImageAndStatusFromList(List<Status> statuses) throws MalformedURLException {
        ImageAndStatus imageAndStatus = null;
        for (Status status : statuses) {
            MediaEntity[] mediaEntities = status.getMediaEntities();
            if (mediaEntities.length > 0 
                && mediaEntities[0].getType().equals("photo")
            ) {
                URL url = new URL(mediaEntities[0].getMediaURL());
                imageAndStatus = new ImageAndStatus(url, status);
                break;
            }
        }
        return imageAndStatus;
    }

    private static String getRandomTrendName() {
        String trendName = null;
        try {
            Trend[] trends = twitter.trends().getPlaceTrends(1).getTrends();
            Random random = new Random();
            Trend trend = trends[random.nextInt(trends.length)];
            trendName = trend.getName();
        }
        catch (TwitterException exception) {
            exception.printStackTrace();
        }
        System.out.println("Trend: " + trendName);
        return trendName;
    }

    private static List<Status> getStatusesFromIds(long[] ids) {
        List<Status> statuses = new LinkedList<>();
        for (long id : ids) {
            try {
                // make the status and add it to statuses
                Status status = twitter.showStatus(id);
                statuses.add(status);
            }
            catch (TwitterException exception) {
                System.out.println("=== Twitter Exception ===");
                exception.printStackTrace();
            }
        }
        return statuses;
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
        long id = originalStatus.getId();
        StatusUpdate statusUpdate = new StatusUpdate("rearranged https://twitter.com/Interior/status/" + id);
        // attach the image
        statusUpdate.setMedia(getUploadableImage(image));
        // mark explict if original status was explict
        statusUpdate.setPossiblySensitive(originalStatus.isPossiblySensitive());
        return statusUpdate;
    }

    private static StatusUpdate setupReply(BufferedImage image, Status originalStatus) {
        String username = originalStatus.getUser().getScreenName();
        StatusUpdate statusUpdate = new StatusUpdate("@" + username + " rearranged the pixels");
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
            // if the png is less than max upload return that
            if (Files.size(tempImg.toPath()) < MAX_UPLOAD_SIZE) {
                output = tempImg;
            }
            else {
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

    public static ImageAndStatus getTrendingImageAndStatus() {
        String searchTerm = getRandomTrendName();
        ImageAndStatus imageAndStatus = null;
        try {
            List<Status> statuses = getStatusesFromIds(
                TwitterApi2.getIdsOfStatusesWithImage(searchTerm)
            );
            imageAndStatus = getImageAndStatusFromList(statuses);
        }
        catch (TwitterException exception) {
            exception.printStackTrace();
        }
        catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        return imageAndStatus;
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
        StatusUpdate statusUpdate = setupReply(image, originalStatus);
        // Set update as a reply to the original status
        // statusUpdate.setInReplyToStatusId(1337965556993982465L);
        statusUpdate = statusUpdate.inReplyToStatusId(originalStatus.getId());
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