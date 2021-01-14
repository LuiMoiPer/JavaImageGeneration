package ImageGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;

import twitter4j.JSONArray;
import twitter4j.JSONObject;
import twitter4j.TwitterException;

public class TwitterApi2 {
    private static String bearerTokenPath = "./Candy/BearerToken";
    private static final String BEARER_TOKEN = getAuthFileContents(bearerTokenPath);

    private static final String HOST = "https://api.twitter.com";
    private static final String RECENT_ENDPOINT = "/2/tweets/search/recent";

    private static final String AUTH_HEADER = "Authorization";

    public static void main(String[] args) {
        try {
            getIdsOfStatusesWithImage("test space");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAuthFileContents(String path) {
        String contents = "";
        try {
            Scanner fileReader = new Scanner(new File(path));
            contents = fileReader.nextLine();
        } catch (FileNotFoundException exception) {
            System.out.println("File not found: " + path);
        }
        return contents;
    }

    private static long[] getIdsFromJson(String json) {
        JSONObject response = new JSONObject(json);
        JSONArray dataEntries = response.getJSONArray("data");
        long[] ids = new long[dataEntries.length()];
        for (int i = 0; i < dataEntries.length(); i++) {
            JSONObject status = dataEntries.getJSONObject(i);
            ids[i] = status.getLong("id");
        }
        return ids;
    }

    public static long[] getIdsOfStatusesWithImage(String searchTerm) throws TwitterException {
        try {
            searchTerm = URLEncoder.encode(searchTerm, "UTF-8");
            String queryString = "?query=" + searchTerm + "%20has:images%20-is:retweet";
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
            requestBuilder.uri(URI.create(HOST + RECENT_ENDPOINT + queryString)).header(AUTH_HEADER,
                    "Bearer " + BEARER_TOKEN);

            HttpResponse<String> response = client.send(requestBuilder.build(), BodyHandlers.ofString());
            if (response.statusCode() < 300) {
                return getIdsFromJson(response.body());
            }
            else {
                throw new TwitterException("Error on call to twitter api");
            }
        } 
        catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        return new long[0];
    }
}
