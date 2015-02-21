package net.year4000.utilities;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class URLBuilder {
    private String url = "";
    private List<String> paths = new LinkedList<>();
    private Map<String, String> queries = new LinkedHashMap<>();

    /** Create a url builder */
    public static URLBuilder builder(String base) {
        URLBuilder builder = new URLBuilder();
        builder.url = base.startsWith("http://") || base.startsWith("https://") ? base : "http://" + base;
        return builder;
    }

    /** Add a path to the base url */
    public URLBuilder addPath(String path) {
        this.paths.addAll(Arrays.asList(path.split("/")));
        return this;
    }

    /** Add a path to the base url */
    public URLBuilder addPath(Number path) {
        return addPath(path.toString());
    }
    
    /** Add an empty path to the base url */
    public URLBuilder addPath() {
        return addPath("");
    }

    /** Add a query key and value to the base url */
    public URLBuilder addQuery(String key, String value) {
        queries.put(key, value);
        return this;
    }

    /** Add a query key and value to the base url */
    public URLBuilder addQuery(String key, Number value) {
        return addQuery(key, value.toString());
    }

    /** Add a query key to the base url */
    public URLBuilder addQuery(String key) {
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
                url += (first ? "?" : "&") + query.getKey() + "=" + query.getValue();
                first = false;
            }
        }

        try {
            return (new URL(url)).toString();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
