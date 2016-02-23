/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.year4000.utilities.locale;

import com.google.gson.Gson;
import net.year4000.utilities.LogUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("unused")
public class URLLocaleManager extends AbstractLocaleManager {
    public static final String LOCALES_JSON = "locales.json";
    private static final Gson gson = new Gson();

    /** Load the url manager from the default url path */
    public URLLocaleManager(URL url) {
        this(url, parseJson(url + LOCALES_JSON));
    }

    /** Load the url manager from the default url path */
    public URLLocaleManager(String url) {
        this(url, parseJson(url + LOCALES_JSON));
    }

    /**
     * Load the locale files once created.
     *
     * @param url     The url object the use.
     * @param locales The locale codes.
     */
    public URLLocaleManager(URL url, String... locales) {
        super(url.toString(), locales);
    }

    /**
     * Load the locale files once created.
     *
     * @param path    The url path ending with a slash.
     * @param locales The locale codes.
     */
    public URLLocaleManager(String path, String... locales) {
        super(path, locales);
    }

    /**
     * Load the locales and set the LogUtil
     *
     * @param log     The LogUtil to use.
     * @param path    The url path ending with a slash.
     * @param locales The locale codes.
     */
    public URLLocaleManager(LogUtil log, String path, String... locales) {
        super(path, locales, log);
    }

    /**
     * Creates an string array based on the provided url.
     *
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
            return new String[]{DEFAULT_LOCALE.toString()};
        }
    }

    /** Load all the locales from the url location. */
    @Override
    protected void loadLocales() {
        for (String code : codes) {
            try {
                URLConnection url = new URL(path + code + EXTENSION).openConnection();
                url.setUseCaches(false);
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
}
