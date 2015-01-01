package net.year4000.utilities.locale;

import net.year4000.utilities.LogUtil;
import com.google.common.base.Charsets;
import lombok.Data;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This abstract class is here for another class to implement
 * you will need to create a singleton pattern to use this
 * correctly.
 */
@Data
@SuppressWarnings("unused")
public abstract class AbstractLocaleManager {
    @Getter
    private final Map<Locale, Properties> locales = new ConcurrentHashMap<>();
    public static final String DEFAULT_LOCALE = "en_US";
    protected static final String EXTENSION = ".properties";
    protected String path;
    protected String[] codes;
    protected LogUtil log;

    /**
     * Init the LocaleManager with a default LogUtil logger.
     * It is then your job to init path and codes.
     */
    public AbstractLocaleManager() {
        log = new LogUtil();
    }

    /**
     * Init the LocaleManager with default LogUtil logger and
     * set the path to get the locales and the codes that this
     * manager will manager.
     */
    @ParametersAreNonnullByDefault
    public AbstractLocaleManager(String path, String... codes) {
        this(path, codes, new LogUtil());
    }

    /**
     * Fully init the locale manager and then load all the default locale codes.
     * @param path The paths that stores the locale files.
     * @param codes The locale codes that we try to grab by default.
     * @param log The log util that this class will use.
     */
    @ParametersAreNonnullByDefault
    public AbstractLocaleManager(String path, String[] codes, LogUtil log) {
        this.path = checkNotNull(path);
        this.codes = checkNotNull(codes);
        this.log = checkNotNull(log);

        loadLocales();
    }

    /** The method that will handle the loading mechanics of the locales */
    protected abstract void loadLocales();

    /**
     * Load a resource to be used, in the locale system.
     * @param key The name of the.
     */
    @ParametersAreNonnullByDefault
    protected void loadLocale(String key, InputStream locale) {
        Properties file = new Properties();

        try {
            file.load(new InputStreamReader(locale, Charsets.UTF_8));
            locales.put(new Locale(key), file);
            checkNotNull(log).debug("LocaleManager Added: " + key);
        } catch (IOException e) {
            checkNotNull(log).log(e, false);
        }
    }

    /**
     * Check is the requested locale is loaded.
     * @param locale The language code in string form.
     * @return true When the language is loaded.
     */
    public boolean isLocale(String locale) {
        return locales.containsKey(new Locale(locale));
    }

    /**
     * Get the properties file for the current locale.
     * @param locale The locale code.
     * @return The properties for the locale.
     */
    public Properties getLocale(String locale) {
        return checkNotNull(locales.get(new Locale(locale)));
    }
}
