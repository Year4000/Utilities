package net.year4000.utilities.locale;

import net.year4000.utilities.MessageUtil;
import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract layout to add simple translation keys.
 * To get started with this you must populate
 * the LocaleManager with your own manager. Then
 * override the constructor to add the manager when
 * this player is loaded.
 */
public abstract class LocaleWrapper implements LocaleUtil {
    public static final String DEFAULT_LOCALE = "en_US";
    @NonNull
    protected AbstractLocaleManager localeManager;
    @Getter
    protected String locale;

    /** Translate to the specific locale with formatting */
    public String get(String key, Object... args) {
        checkNotNull(localeManager);

        if (localeManager.getLocales().size() == 0 || !localeManager.isLocale(DEFAULT_LOCALE)) {
            return "(" + locale + ") " + key + " " + Joiner.on(", ").join(args);
        }
        else if (!localeManager.isLocale(locale)) {
            locale = DEFAULT_LOCALE;
        }

        return MessageUtil.replaceColors(String.format(localeManager.getLocale(locale).getProperty(key, key + (args.length > 0 ? " " + Joiner.on(", ").join(args) : "")), args));
    }

}
