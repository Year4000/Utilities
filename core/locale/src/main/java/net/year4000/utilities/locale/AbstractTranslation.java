/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.locale;

import com.google.common.base.Joiner;
import lombok.Getter;

import java.util.Locale;
import java.util.MissingFormatArgumentException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract layout to add simple translation keys.
 * To get started with this you must populate
 * the LocaleManager with your own manager. Then
 * override the constructor to add the manager when
 * this player is loaded.
 */
public abstract class AbstractTranslation implements Translatable {
    public static final Locale DEFAULT_LOCALE = Locale.US;
    public static final String DEFAULT_LOCALE_STRING = DEFAULT_LOCALE.toString();
    protected AbstractLocaleManager localeManager;
    @Getter
    protected String locale;

    /** Force the wrapper to create the instance */
    public AbstractTranslation(AbstractLocaleManager localeManager, String locale) {
        this.localeManager = checkNotNull(localeManager);
        this.locale = checkNotNull(locale);
    }

    /** Translate to the specific locale with formatting */
    public String get(String key, Object... args) {
        if (localeManager.getLocales().size() == 0 || !localeManager.isLocale(DEFAULT_LOCALE_STRING)) {
            return "(" + locale + ") " + key + " " + Joiner.on(", ").join(args);
        }
        else if (!localeManager.isLocale(locale)) {
            locale = DEFAULT_LOCALE_STRING;
        }

        String formatted, missingKey = key + (args.length > 0 ? " " + Joiner.on(", ").join(args) : "");

        try {
            formatted = String.format(localeManager.getLocale(locale).getProperty(key, missingKey), args);
        }
        catch (MissingFormatArgumentException error) {
            formatted = localeManager.getLocale(locale).getProperty(key, missingKey);
        }

        return formatted;
    }
}
