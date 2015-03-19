package net.year4000.utilities.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.year4000.utilities.Callback;
import net.year4000.utilities.scheduler.SchedulerManager;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpFetcher {
    private static final String USER_AGENT = "Year4000 Utilities API Interface";
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final SchedulerManager SCHEDULER = new SchedulerManager();
    private enum Methods {GET, POST, PUT, DELETE}

    /** Normal data request method that only return data */
    private static Reader request(Methods method, String uri) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(uri).openConnection();
        connection.setRequestMethod(method.name());
        connection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode;

        // Get Response
        if ((responseCode = connection.getResponseCode()) != 200) {
            throw new IOException(responseCode + " " + connection.getResponseMessage());
        }
        else {
            return new InputStreamReader(connection.getInputStream());
        }
    }

    /** Data request method that give data and returns data */
    private static Reader request(Methods method, JsonObject object, String uri) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(uri).openConnection();
        connection.setRequestMethod(method.name());
        connection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setDoOutput(true);
        int responseCode;

        // Send Data
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            GSON.toJson(object, writer);
        }

        // Get Response
        if ((responseCode = connection.getResponseCode()) != 200) {
            throw new IOException(responseCode + " " + connection.getResponseMessage());
        }
        else {
            return new InputStreamReader(connection.getInputStream());
        }
    }

    /** HTTP get method with async request */
    public static <T> void get(String url, Class<T> clazz, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = get(url, clazz);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP get method with async request */
    public static <T> void get(String url, Type type, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = get(url, type);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP get method with sync request */
    public static <T> T get(String url, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.GET, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP get method with sync request */
    public static <T> T get(String url, Type type) throws Exception {
        try (Reader reader = request(Methods.GET, url)) {
            return GSON.fromJson(reader, type);
        }
    }

    /** HTTP post method with async request */
    public static <T> void post(String url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = post(url, data, clazz);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP post method with async request */
    public static <T> void post(String url, JsonObject data, Type type, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = post(url, data, type);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP post method with sync request */
    public static <T> T post(String url, JsonObject data, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.POST, data, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP post method with sync request */
    public static <T> T post(String url, JsonObject data, Type type) throws Exception {
        try (Reader reader = request(Methods.POST, data, url)) {
            return GSON.fromJson(reader, type);
        }
    }

    /** HTTP put method with async request */
    public static <T> void put(String url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = post(url, data, clazz);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP put method with async request */
    public static <T> void put(String url, JsonObject data, Type type, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = post(url, data, type);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP put method with sync request */
    public static <T> T put(String url, JsonObject data, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.PUT, data, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP put method with sync request */
    public static <T> T put(String url, JsonObject data, Type type) throws Exception {
        try (Reader reader = request(Methods.PUT, data, url)) {
            return GSON.fromJson(reader, type);
        }
    }

    /** HTTP delete method with async request */
    public static <T> void delete(String url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = post(url, data, clazz);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP delete method with async request */
    public static <T> void delete(String url, JsonObject data, Type type, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = post(url, data, type);
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                callback.callback(response, error);
            }
        });
    }

    /** HTTP delete method with sync request */
    public static <T> T delete(String url, JsonObject data, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.DELETE, data, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP delete method with sync request */
    public static <T> T delete(String url, JsonObject data, Type type) throws Exception {
        try (Reader reader = request(Methods.DELETE, data, url)) {
            return GSON.fromJson(reader, type);
        }
    }
}
