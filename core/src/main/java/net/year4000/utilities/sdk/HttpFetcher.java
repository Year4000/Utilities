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
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpFetcher {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final SchedulerManager SCHEDULER = new SchedulerManager();
    private enum Methods {GET, POST, PUT, DELETE}

    /** Normal data request method that only return data */
    private static Reader request(Methods method, HttpConnection uri) throws IOException {
        // Use secured https if url is https
        if (uri.getUrlBuilder().isSecured()) {
            HttpsURLConnection connection = uri.getConnection();
            connection.setRequestMethod(method.name());

            // Get Response
            return HttpConnection.responseHttps(connection);
        }

        HttpURLConnection connection = uri.getConnection();
        connection.setRequestMethod(method.name());

        // Get Response
        return HttpConnection.responseHttp(connection);
    }

    /** Data request method that give data and returns data */
    private static Reader request(Methods method, JsonObject object, HttpConnection uri) throws IOException {
        // Use secured https if url is https
        if (uri.getUrlBuilder().isSecured()) {
            HttpsURLConnection connection = uri.getConnection();
            connection.setRequestMethod(method.name());
            connection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            connection.setDoOutput(true);

            HttpConnection.requestHttps(connection, object);

            return HttpConnection.responseHttps(connection);
        }

        HttpURLConnection connection = uri.getConnection();
        connection.setRequestMethod(method.name());
        connection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        connection.setDoOutput(true);

        HttpConnection.requestHttp(connection, object);

        return HttpConnection.responseHttp(connection);
    }

    /** HTTP get method with async request */
    public static <T> void get(HttpConnection url, Class<T> clazz, Callback<T> callback) {
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
    public static <T> void get(String url, Class<T> clazz, Callback<T> callback) {
        get(new HttpConnection(url), clazz, callback);
    }

    /** HTTP get method with async request */
    public static <T> void get(HttpConnection url, Type type, Callback<T> callback) {
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

    /** HTTP get method with async request */
    public static <T> void get(String url, Type type, Callback<T> callback) {
        get(new HttpConnection(url), type, callback);
    }

    /** HTTP get method with sync request */
    public static <T> T get(HttpConnection url, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.GET, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP get method with sync request */
    public static <T> T get(String url, Class<T> clazz) throws Exception {
        return get(new HttpConnection(url), clazz);
    }

    /** HTTP get method with sync request */
    public static <T> T get(HttpConnection url, Type type) throws Exception {
        try (Reader reader = request(Methods.GET, url)) {
            return GSON.fromJson(reader, type);
        }
    }

    /** HTTP get method with sync request */
    public static <T> T get(String url, Type type) throws Exception {
        return get(new HttpConnection(url), type);
    }

    /** HTTP post method with async request */
    public static <T> void post(HttpConnection url, JsonObject data, Class<T> clazz, Callback<T> callback) {
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
    public static <T> void post(String url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        post(new HttpConnection(url), data, clazz, callback);
    }

    /** HTTP post method with async request */
    public static <T> void post(HttpConnection url, JsonObject data, Type type, Callback<T> callback) {
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

    /** HTTP post method with async request */
    public static <T> void post(String url, JsonObject data, Type type, Callback<T> callback) {
        post(new HttpConnection(url), data, type, callback);
    }

    /** HTTP post method with sync request */
    public static <T> T post(HttpConnection url, JsonObject data, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.POST, data, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP post method with sync request */
    public static <T> T post(String url, JsonObject data, Class<T> clazz) throws Exception {
        return post(new HttpConnection(url), data, clazz);
    }

    /** HTTP post method with sync request */
    public static <T> T post(HttpConnection url, JsonObject data, Type type) throws Exception {
        try (Reader reader = request(Methods.POST, data, url)) {
            return GSON.fromJson(reader, type);
        }
    }

    /** HTTP post method with sync request */
    public static <T> T post(String url, JsonObject data, Type type) throws Exception {
        return post(new HttpConnection(url), data, type);
    }

    /** HTTP put method with async request */
    public static <T> void put(HttpConnection url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = put(url, data, clazz);
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
    public static <T> void put(String url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        put(new HttpConnection(url), data, clazz, callback);
    }

    /** HTTP put method with async request */
    public static <T> void put(HttpConnection url, JsonObject data, Type type, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = put(url, data, type);
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
        put(new HttpConnection(url), data, type, callback);
    }

    /** HTTP put method with sync request */
    public static <T> T put(HttpConnection url, JsonObject data, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.PUT, data, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP put method with sync request */
    public static <T> T put(String url, JsonObject data, Class<T> clazz) throws Exception {
        return put(new HttpConnection(url), data, clazz);
    }

    /** HTTP put method with sync request */
    public static <T> T put(HttpConnection url, JsonObject data, Type type) throws Exception {
        try (Reader reader = request(Methods.PUT, data, url)) {
            return GSON.fromJson(reader, type);
        }
    }

    /** HTTP put method with sync request */
    public static <T> T put(String url, JsonObject data, Type type) throws Exception {
        return put(new HttpConnection(url), data, type);
    }

    /** HTTP delete method with async request */
    public static <T> void delete(HttpConnection url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = delete(url, data, clazz);
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
    public static <T> void delete(String url, JsonObject data, Class<T> clazz, Callback<T> callback) {
        delete(new HttpConnection(url), data, clazz, callback);
    }

    /** HTTP delete method with async request */
    public static <T> void delete(HttpConnection url, JsonObject data, Type type, Callback<T> callback) {
        SCHEDULER.run(() -> {
            T response = null;
            Throwable error = null;

            try {
                response = delete(url, data, type);
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
        delete(new HttpConnection(url), data, type, callback);
    }

    /** HTTP delete method with sync request */
    public static <T> T delete(HttpConnection url, JsonObject data, Class<T> clazz) throws Exception {
        try (Reader reader = request(Methods.DELETE, data, url)) {
            return GSON.fromJson(reader, clazz);
        }
    }

    /** HTTP delete method with sync request */
    public static <T> T delete(String url, JsonObject data, Class<T> clazz) throws Exception {
        return delete(new HttpConnection(url), data, clazz);
    }

    /** HTTP delete method with sync request */
    public static <T> T delete(HttpConnection url, JsonObject data, Type type) throws Exception {
        try (Reader reader = request(Methods.DELETE, data, url)) {
            return GSON.fromJson(reader, type);
        }
    }

    /** HTTP delete method with sync request */
    public static <T> T delete(String url, JsonObject data, Type type) throws Exception {
        return delete(new HttpConnection(url), data, type);
    }
}
