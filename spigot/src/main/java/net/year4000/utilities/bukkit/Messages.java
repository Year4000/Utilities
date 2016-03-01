/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.bukkit;

import net.year4000.utilities.cache.QuickCache;
import net.year4000.utilities.locale.LocaleKeys;
import net.year4000.utilities.locale.Translatable;
import net.year4000.utilities.locale.URLLocaleManager;

import java.util.Locale;
import java.util.Optional;

public enum Messages implements LocaleKeys<Locale, String> {
    // Locale Header
    LOCALE_CODE,
    LOCALE_NAME,

    // Commands
    CMD_ERROR_PERMISSION,
    CMD_ERROR_USAGE,
    CMD_ERROR_NUMBER,
    CMD_ERROR,
    ;

    @Override
    public Translatable<String> apply(Optional<Locale> locale) {
        return new BukkitLocale(Factory.inst.get(), locale);
    }

    /** The factory to handle Locale Managers */
    public static class Factory extends URLLocaleManager {
        static QuickCache<Factory> inst = QuickCache.builder(Messages.Factory.class).build();

        public Factory() {
            super("https://raw.githubusercontent.com/Year4000/Locales/master/utilities/");
        }
    }
}
