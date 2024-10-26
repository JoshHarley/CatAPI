import com.google.gson.JsonArray;

import java.net.URL;

public record CatImages(String id, URL url, JsonArray breeds, int width, int height) {

}
