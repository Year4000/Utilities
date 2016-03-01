/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import com.google.common.base.Joiner;
import lombok.Getter;
import net.year4000.utilities.locale.AbstractLocaleManager;
import net.year4000.utilities.locale.Translatable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Locale;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

import static net.year4000.utilities.locale.AbstractLocaleManager.DEFAULT_LOCALE;
import static org.spongepowered.api.text.format.TextColors.*;

public class SpongeLocale implements Translatable<Text> {
    private AbstractLocaleManager localeManager;
    @Getter
    private Locale locale;

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
        this.localeManager = localeManager;
        this.locale = new Locale(locale.orElse(DEFAULT_LOCALE).toString().toLowerCase());
    }

    @Override
    public Text get(String key, Object... args) {
        if (localeManager.getLocales().size() == 0 || !localeManager.isLocale(DEFAULT_LOCALE)) {
            return Text.of(
                GRAY, "(", YELLOW, locale, GRAY, ") ",
                DARK_GREEN, key, GREEN, " ",
                Joiner.on(", ").join(args)
            );
        }

        String missingKey = key + (args.length > 0 ? " " + Joiner.on(", ").join(args) : "");
        String translation = localeManager.getLocale(locale).getProperty(key, missingKey);
        Text formatted;

        try {
            formatted = Text.builder(translation).build(args);
        }
        catch (MissingFormatArgumentException error) {
            formatted = Text.of(translation);
        }

        return formatted;
    }
}
