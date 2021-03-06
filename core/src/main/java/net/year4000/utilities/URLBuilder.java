/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import com.google.common.base.Joiner;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public final class URLBuilder {
    private String url = "";
    private List<String> paths = new LinkedList<>();
    private Map<String, String> queries = new LinkedHashMap<>();

    private URLBuilder() {}

    /** Create a url builder */
    public static URLBuilder builder(String base) {
        Conditions.nonNull(base, "base");
        URLBuilder builder = new URLBuilder();
        builder.url = base.startsWith("http://") || base.startsWith("https://") ? base : "http://" + base;
        return builder;
    }

    /** Copy a url builder */
    public static URLBuilder builder(URLBuilder builder) {
        Conditions.nonNull(builder, "builder");
        URLBuilder copy = new URLBuilder();
        copy.url = builder.url;
        copy.paths = new LinkedList<>(builder.paths);
        copy.queries = new LinkedHashMap<>(builder.queries);

        return copy;
    }

    /** Create a URL Builder instance from url */
    public static URLBuilder fromURL(String url) {
        Conditions.nonNullOrEmpty(url, "url");
        String[] uri = url.startsWith("http://") || url.startsWith("https://") ? url.split("://") : new String[]{"http", url};
        String[] paths = uri[1].contains("/") ? uri[1].split("/") : new String[]{uri[1]};
        URLBuilder copy = URLBuilder.builder(uri[0] + "://" + paths[0]);
        copy.paths = new LinkedList<>();
        copy.queries = new LinkedHashMap<>();

        for (int i = 1; i < paths.length; i++) {
            // Does the path contains a query string
            if (i == paths.length - 1 && paths[i].contains("?")) {
                String[] split = paths[i].split("[\\?&]");
                copy.paths.add(split[0]);

                // Add all the queries to the url
                for (int j = 1; j < split.length; j++) {
                    if (split[j].contains("=")) {
                        String[] entry = split[j].split("=");
                        copy.queries.put(entry[0], entry[1]);
                    }
                    else {
                        copy.queries.put(split[j], "");
                    }
                }
            }
            else {
                copy.paths.add(paths[i]);
            }
        }

        return copy;
    }

    /** Is the url builder an instance of a secured url */
    public boolean isSecured() {
        return url.startsWith("https");
    }

    /** Add a path to the base url */
    public URLBuilder addPath(String path) {
        Conditions.nonNull(path, "path"); // Paths can be empty strings
        this.paths.addAll(Arrays.asList(path.split("/")));
        return this;
    }

    /** Add a path to the base url */
    public URLBuilder addPath(Number path) {
        Conditions.nonNull(path, "path");
        return addPath(path.toString());
    }

    /** Add an empty path to the base url */
    public URLBuilder addPath() {
        return addPath("");
    }

    /** Add a query key and value to the base url */
    public URLBuilder addQuery(String key, String value) {
        Conditions.nonNullOrEmpty(key, "key");
        Conditions.nonNull(value, "value"); // value can be empty strings
        queries.put(key, value);
        return this;
    }

    /** Add a query key and value to the base url */
    public URLBuilder addQuery(String key, Number value) {
        Conditions.nonNullOrEmpty(key, "key");
        Conditions.nonNull(value, "value");
        return addQuery(key, value.toString());
    }

    /** Add a query key to the base url */
    public URLBuilder addQuery(String key) {
        Conditions.nonNullOrEmpty(key, "key");
        return addQuery(key, "");
    }

    /** Create the url string */
    public String build() {
        String url = this.url;

        if (paths.size() > 0) {
            url = url.endsWith("/") ? url : url + "/";
            url += Joiner.on('/').join(paths);
        }
        else {
            url = url.endsWith("/") ? url : url + "/";
        }

        if (queries.size() > 0) {
            boolean first = true;

            for (Map.Entry<String, String> query : queries.entrySet()) {
                try {
                    String value = URLEncoder.encode(query.getValue(), "utf-8");
                    url += (first ? "?" : "&") + query.getKey() + "=" + value;
                    first = false;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return (new URL(url)).toString();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }
}
