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

import net.year4000.utilities.MessageUtil;
import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.NonNull;

import java.util.MissingFormatArgumentException;

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

        String formatted, missingKey = key + (args.length > 0 ? " " + Joiner.on(", ").join(args) : "");

        try {
            formatted = String.format(localeManager.getLocale(locale).getProperty(key, missingKey), args);
        }
        catch (MissingFormatArgumentException error) {
            formatted = localeManager.getLocale(locale).getProperty(key, missingKey);
        }

        return MessageUtil.replaceColors(formatted);
    }
}
