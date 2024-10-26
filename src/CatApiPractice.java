import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CatApiPractice {

    public static void main(String[] args) {
        catBreeds = getBreeds();
        DisplayView displayView = new DisplayView(catBreeds, images, client);
        displayView.display();
    }
    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .version(HttpClient.Version.HTTP_2)
            .build();
    private static Map<String, Cat> catBreeds = new HashMap<>();
    private static final Map<String, ImageIcon> images = new HashMap<>();

    /**
     * A method for fetching the breed information from the api
     * @return - Map<String, Cat>
     */
    public static Map<String, Cat> getBreeds(){
        Map<String, Cat> catMap = new HashMap<>();

        HttpRequest request = sendRequest("breeds");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.body().length() > 1040){
                ArrayList responseArray = gson.fromJson(response.body(), ArrayList.class);
                for (Object breed : responseArray) {
                    String json = gson.toJson(breed);
                    Cat cat = gson.fromJson(json, Cat.class);
                    catMap.put(cat.getName(), cat);
                }
            } else {
                Cat cat = gson.fromJson(response.body(), Cat.class);
                catMap.put(cat.getName(), cat);
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return catMap;
    }

    /**
     * A method designed to set up the api request
     * @param parameter - A String for defining the request
     * @return - HttpRequest
     */
    public static HttpRequest sendRequest(String parameter){
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.thecatapi.com/v1/" + parameter))
                .header("x-api-key", "live_3B6P7ylm3xkwmozfUJMdSWFi2ksFuzA3sguSO9gEXmRYT4SKuL67oN9lmsAAAyVF")
                .build();
    }
}
