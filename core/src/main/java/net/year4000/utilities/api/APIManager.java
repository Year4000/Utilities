package net.year4000.utilities.api;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class APIManager {

    @Getter(AccessLevel.PUBLIC)
    private static Gson gson = new Gson();
    @Setter(AccessLevel.PUBLIC)
    private static String apikey = "";

    private static HashMap<String, String> cache = new HashMap<>();
    static {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cache.clear();
            }
        }, 0, 5000);
    }

    public static API getAPI() {
        return readJson("https://api.year4000.net", API.class);
    }

    private static String readUrl(String urlString) throws IOException {
        urlString += (urlString.contains("?") ? "&" : "?") + "key=" + apikey + "&compact";
        if (cache.containsKey(urlString)) {
            System.err.println(urlString);
            return cache.get(urlString);
        }
        System.out.println(urlString);
        BufferedReader reader = null;
        URL url = new URL(urlString);
        reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder buffer = new StringBuilder();
        int read;
        char[] chars = new char[1024];
        while ((read = reader.read(chars)) != -1) {
            buffer.append(chars, 0, read);
        }
        reader.close();
        cache.put(urlString, buffer.toString());
        return cache.get(urlString);
    }

    protected static <T> T readJson(String url, Class<T> clazz) {
        try {
            return getGson().fromJson(readUrl(url), clazz);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return getGson().fromJson("{}", clazz);
    }

    public static String getRouteUrl(String route) {
        String temp = getAPI().getRoutes().get(route);
        return temp.contains("{") ? temp.substring(0, temp.indexOf("{")) : temp;
    }
}
