/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.locale.AbstractLocaleManager;
import net.year4000.utilities.locale.AbstractTranslation;
import org.spongepowered.api.command.CommandSource;

import java.util.Locale;
import java.util.Optional;

import static net.year4000.utilities.locale.AbstractLocaleManager.DEFAULT_LOCALE;

public class SpongeLocale extends AbstractTranslation {
    /** Create a new instance for Sponge Locales */
    public SpongeLocale(AbstractLocaleManager localeManager) {
        this(localeManager, Optional.empty());
    }

    /** Create a new instance for Sponge Locales */
    public SpongeLocale(AbstractLocaleManager localeManager, CommandSource source) {
        this(localeManager, Optional.of(source.getLocale()));
    }

    /** Create a new instance for Sponge Locales */
    public SpongeLocale(AbstractLocaleManager localeManager, Optional<Locale> locale) {
        super(localeManager, locale.orElse(DEFAULT_LOCALE).toString());
    }
}
