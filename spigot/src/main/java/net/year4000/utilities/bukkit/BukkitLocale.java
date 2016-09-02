/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit;

import static net.year4000.utilities.locale.AbstractLocaleManager.DEFAULT_LOCALE;

import net.year4000.utilities.locale.AbstractLocaleManager;
import net.year4000.utilities.locale.StringTranslatable;

import java.util.Locale;
import java.util.Optional;

@SuppressWarnings("unused")
public class BukkitLocale extends StringTranslatable {
    /** Start creating locales for the specific player's locale */
    public BukkitLocale(AbstractLocaleManager localeManager, Optional<Locale> locale) {
        super(localeManager, locale.orElse(DEFAULT_LOCALE).toString());
    }

    /** Translate to the specific locale with formatting */
    public String get(String key, Object... args) {
        return MessageUtil.replaceColors(super.get(key, args));
    }
}
