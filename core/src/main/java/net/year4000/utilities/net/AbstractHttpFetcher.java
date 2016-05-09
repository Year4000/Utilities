/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import net.year4000.utilities.Builder;
import net.year4000.utilities.Callback;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.value.Value;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;

/** This HttpFetcher will fetch a Http Request */
public abstract class AbstractHttpFetcher<D> implements HttpFetcher<D> {
    protected final int MAX_TRIES;
    protected final ExecutorService EXECUTOR;
    private String contentType;

    protected AbstractHttpFetcher(int maxTries, ExecutorService executorService) {
        MAX_TRIES = Conditions.isLarger(maxTries, 0);
        EXECUTOR = Conditions.nonNull(executorService, "executorService");
        ContentType content = getClass().getAnnotation(ContentType.class);
        contentType = Conditions.nonNull(content, "Derived classes must include @ContentType").value();
    }

    /** Normal data request method that only return data */
    protected Reader request(Methods method, HttpConnection uri) throws IOException {
        return request(method, uri, 1);
    }

    /** Normal data request method that only return data, try until max tries has been reached */
    protected Reader request(Methods method, HttpConnection uri, int tries) throws IOException {
        Conditions.nonNull(method, "method");
        Conditions.nonNull(uri, "uri");
        // Use secured https if url is https
        try {
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
        catch (IOException error) {
            if (tries < MAX_TRIES) {
                return request(method, uri.clone(), ++tries);
            }
            else {
                throw error;
            }
        }
    }

    /** Data request method that give data and returns data */
    protected Reader request(Methods method, D object, HttpConnection uri) throws IOException {
        return request(method, object, uri, 1);
    }

    /** Data request method that give data and returns data, try until max tries has been reached */
    protected Reader request(Methods method, D object, HttpConnection uri, int tries) throws IOException {
        Conditions.nonNull(method, "method");
        Conditions.nonNull(object, "object");
        try {
            // Use secured https if url is https
            if (uri.getUrlBuilder().isSecured()) {
                HttpsURLConnection connection = uri.getConnection();
                connection.setRequestMethod(method.name());
                connection.setRequestProperty("Content-Type", contentType);
                connection.setDoOutput(true);

                HttpConnection.requestHttps(connection, serialize(object));

                return HttpConnection.responseHttps(connection);
            }

            HttpURLConnection connection = uri.getConnection();
            connection.setRequestMethod(method.name());
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoOutput(true);

            HttpConnection.requestHttp(connection, serialize(object));

            return HttpConnection.responseHttp(connection);
        }
        catch (IOException error) {
            if (tries < MAX_TRIES) {
                return request(method, object, uri.clone(), ++tries);
            }
            else {
                throw error;
            }
        }
    }

    /** HTTP get method with async request */
    @Override
    public <T> void get(HttpConnection url, Type type, Callback<T> callback) {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(type, "type");
        Conditions.nonNull(callback, "callback");
        EXECUTOR.execute(() -> {
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
    @Override
    public <T> T get(HttpConnection url, Type type) throws Exception {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(type, "type");
        try (Reader reader = request(Methods.GET, url)) {
            return reader(reader, type);
        }
    }

    /** HTTP post method with async request */
    @Override
    public <T> void post(HttpConnection url, D data, Type type, Callback<T> callback) {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(data, "data");
        Conditions.nonNull(type, "type");
        Conditions.nonNull(callback, "callback");
        EXECUTOR.execute(() -> {
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
    @Override
    public <T> T post(HttpConnection url, D data, Type type) throws Exception {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(data, "data");
        Conditions.nonNull(type, "type");
        try (Reader reader = request(Methods.POST, data, url)) {
            return reader(reader, type);
        }
    }

    /** HTTP put method with async request */
    @Override
    public <T> void put(HttpConnection url, D data, Type type, Callback<T> callback) {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(data, "data");
        Conditions.nonNull(type, "type");
        Conditions.nonNull(callback, "callback");
        EXECUTOR.execute(() -> {
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

    /** HTTP put method with sync request */
    @Override
    public <T> T put(HttpConnection url, D data, Type type) throws Exception {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(data, "data");
        Conditions.nonNull(type, "type");
        try (Reader reader = request(Methods.PUT, data, url)) {
            return reader(reader, type);
        }
    }

    /** HTTP delete method with async request */
    @Override
    public <T> void delete(HttpConnection url, D data, Type type, Callback<T> callback) {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(data, "data");
        Conditions.nonNull(type, "type");
        Conditions.nonNull(callback, "callback");
        EXECUTOR.execute(() -> {
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

    /** HTTP delete method with sync request */
    @Override
    public <T> T delete(HttpConnection url, D data, Type type) throws Exception {
        Conditions.nonNull(url, "url");
        Conditions.nonNull(data, "data");
        Conditions.nonNull(type, "type");
        try (Reader reader = request(Methods.DELETE, data, url)) {
            return reader(reader, type);
        }
    }

    /** Make sure the derived class know how to read the data for {@link Type} */
    protected abstract <T> T reader(Reader reader, Type type);

    /** Make sure the derived class know how to read the data for {@link Class} */
    protected abstract <T> T reader(Reader reader, Class<T> type);

    /** Allow the data to be serialize by derived class */
    protected abstract String serialize(D object);

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface ContentType {
        /** The content type that will be used when sending data to the server */
        String value();
    }

    /** A abstract builder that will help in creating {@link AbstractBuilder} */
    @SuppressWarnings("unchecked")
    public abstract static class AbstractBuilder<T, R extends AbstractBuilder> implements Builder<T> {
        protected Value<ExecutorService> executorService = Value.empty();
        protected Value<Integer> maxTries = Value.empty();

        /** Set the executor service for the callback function */
        public R executorService(ExecutorService executorService) {
            this.executorService = Value.of(executorService);
            return (R) this;
        }

        /** Set the max tries to fetch a resource before throwing an error */
        public R maxTries(int maxTries) {
            this.maxTries = Value.of(Math.max(1, maxTries));
            return (R) this;
        }
    }
}
