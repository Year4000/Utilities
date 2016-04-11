/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import com.google.common.collect.Maps;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.URLBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * The connection used to use along side HttpFetcher.
 */
public class HttpConnection implements Cloneable {
    private static final int TIMEOUT = 5000;
    private static final String USER_AGENT = "Year4000 Utilities API Interface";
    private final URLBuilder urlBuilder;
    // Connections
    private Map<String, String> headers = Maps.newLinkedHashMap();
    private String userAgent = null;
    private int timeout = -1;
    // Connection
    private HttpURLConnection urlConnection;

    public HttpConnection(String url) {
        urlBuilder = URLBuilder.fromURL(Conditions.nonNullOrEmpty(url, "url"));
    }

    /** Copy the supplied http connection */
    private HttpConnection(HttpConnection connection) {
        Conditions.nonNull(connection, "connection");
        urlBuilder = connection.urlBuilder;
        headers = connection.headers;
        userAgent = connection.userAgent;
        timeout = connection.timeout;
    }

    /** Response with http */
    public static Reader responseHttp(HttpURLConnection connection) throws IOException {
        int responseCode = Conditions.nonNull(connection, "connection").getResponseCode();

        // Get Response
        if (responseCode >= HttpURLConnection.HTTP_OK && responseCode <= HttpURLConnection.HTTP_PARTIAL) {
            return new InputStreamReader(connection.getInputStream());
        }
        else {
            throw new IOException(responseCode + " " + connection.getResponseMessage());
        }
    }

    /** Response with https */
    public static Reader responseHttps(HttpsURLConnection connection) throws IOException {
        int responseCode = Conditions.nonNull(connection, "connection").getResponseCode();

        // Get Response
        if (responseCode >= HttpsURLConnection.HTTP_OK && responseCode <= HttpsURLConnection.HTTP_PARTIAL) {
            return new InputStreamReader(connection.getInputStream());
        }
        else {
            throw new IOException(responseCode + " " + connection.getResponseMessage());
        }
    }

    /** Send data to the connection https */
    public static void requestHttps(HttpsURLConnection connection, String data) throws IOException {
        Conditions.nonNull(connection, "connection");
        Conditions.nonNullOrEmpty(data, "data");
        // Send Data
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(data);
        }
    }

    /** Send data to the connection https */
    public static void requestHttp(HttpURLConnection connection, String data) throws IOException {
        Conditions.nonNull(connection, "connection");
        Conditions.nonNullOrEmpty(data, "data");
        // Send Data
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(data);
        }
    }

    /** Get or create new connection that only throws RuntimeIOException */
    public <T extends HttpURLConnection> T getSuppressedConnection() {
        try {
            return getConnection();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Get or create new connection */
    public <T extends HttpURLConnection> T getConnection() throws IOException {
        if (urlConnection == null) {
            urlConnection = this.<T>newConnection();
        }

        return (T) urlConnection;
    }

    /** Create a new HTTPURLConnection from this class */
    private <T extends HttpURLConnection> T newConnection() throws IOException {
        T connection = (T) (new URL(urlBuilder.build())).openConnection();

        // Add headers
        headers.forEach(connection::setRequestProperty);

        // Other connection settings
        connection.setRequestProperty("User-Agent", userAgent == null ? USER_AGENT : userAgent);
        connection.setConnectTimeout(timeout < 0 ? TIMEOUT : timeout);
        connection.setReadTimeout(timeout < 0 ? TIMEOUT : timeout);

        return connection;
    }

    /** Clone this object just runs the copy constructor */
    public HttpConnection clone() {
        return new HttpConnection(this);
    }

    public URLBuilder getUrlBuilder() {
        return this.urlBuilder;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = Conditions.nonNull(headers, "headers");
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = Conditions.nonNullOrEmpty(userAgent, "userAgent");
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout < 0 ? -1 : timeout;
    }

    @Override
    public String toString() {
        return Conditions.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Conditions.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Conditions.hashCode(this);
    }
}
