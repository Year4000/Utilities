package net.year4000.utilities.locale;

import net.year4000.utilities.LogUtil;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@NoArgsConstructor
@SuppressWarnings("unused")
public class URLLocaleManager extends AbstractLocaleManager {
    public static final String LOCALES_JSON = "locales.json";
    private static final Gson gson = new Gson();

    /**
     * Load the locale files once created.
     * @param url The url object the use.
     * @param locales The locale codes.
     */
    @ParametersAreNonnullByDefault
    public URLLocaleManager(URL url, String... locales) {
        super(url.toString(), locales);
    }

    /**
     * Load the locale files once created.
     * @param path The url path ending with a slash.
     * @param locales The locale codes.
     */
    @ParametersAreNonnullByDefault
    public URLLocaleManager(String path, String... locales) {
        super(path, locales);
    }

    /**
     * Load the locales and set the LogUtil
     * @param log The LogUtil to use.
     * @param path The url path ending with a slash.
     * @param locales The locale codes.
     */
    @ParametersAreNonnullByDefault
    public URLLocaleManager(LogUtil log, String path, String... locales) {
        super(path, locales, log);
    }

    /** Load all the locales from the url location. */
    protected void loadLocales() {
        for (String code : codes) {
            try {
                URLConnection url = new URL(path + code + EXTENSION).openConnection();
                url.setUseCaches(false);
                url.setDoInput(true);
                url.setDoInput(true);
                url.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
                url.connect();

                loadLocale(code, url.getInputStream());
            }
            catch (Exception e) {
                log.log(e, true);
            }
        }
    }

    /**
     * Creates an string array based on the provided url.
     * @param url The url that is a json object
     * @return String[] of locale codes, or default to DEFAULT_LOCALE
     */
    public static String[] parseJson(String url) {
        try {
            URLConnection con = new URL(url).openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.connect();

            return gson.fromJson(new InputStreamReader(con.getInputStream()), String[].class);
        }
        catch (IOException e) {
            return new String[] {DEFAULT_LOCALE};
        }
    }
}
