/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.locale;

import com.google.common.base.Joiner;

import java.util.Locale;
import java.util.MissingFormatArgumentException;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.year4000.utilities.locale.AbstractLocaleManager.DEFAULT_LOCALE;

public class StringTranslatable implements Translatable<String> {
    protected AbstractLocaleManager localeManager;
    protected Locale locale;

    /** Force the wrapper to create the instance */
    public StringTranslatable(AbstractLocaleManager localeManager, String locale) {
        this(localeManager, new Locale(locale));
    }

    /** Force the wrapper to create the instance */
    public StringTranslatable(AbstractLocaleManager localeManager, Locale locale) {
        this.localeManager = checkNotNull(localeManager);
        this.locale = checkNotNull(locale);
    }

    /** Translate to the specific locale with formatting */
    public String get(String key, Object... args) {
        if (localeManager.getLocales().size() == 0 || !localeManager.isLocale(DEFAULT_LOCALE)) {
            return "(" + locale + ") " + key + " " + Joiner.on(", ").join(args);
        }
        else if (!localeManager.isLocale(locale)) {
            locale = DEFAULT_LOCALE;
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

    public Locale getLocale() {
        return this.locale;
    }
}
