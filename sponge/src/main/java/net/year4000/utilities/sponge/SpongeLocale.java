/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.locale.AbstractLocaleManager;
import net.year4000.utilities.locale.Translatable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Locale;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

import static net.year4000.utilities.locale.AbstractLocaleManager.DEFAULT_LOCALE;
import static org.spongepowered.api.text.format.TextColors.*;
import static org.spongepowered.api.text.serializer.TextSerializers.FORMATTING_CODE;

public class SpongeLocale implements Translatable<Text> {
    private AbstractLocaleManager localeManager;
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
        // No locales format like this
        if (localeManager.getLocales().size() == 0 || !localeManager.isLocale(DEFAULT_LOCALE)) {
            Text base = Text.of(GRAY, "(", YELLOW, locale, GRAY, ") ", DARK_GREEN, key, GREEN, " ");
            return Text.join(base, appender(args, Text.of(GRAY, ", ", GREEN)));
        }

        String translation = localeManager.getLocale(locale).getProperty(key);

        // If no translation format like this
        if (translation == null) {
            Text base = Text.of(DARK_GREEN, key, GREEN, " ");
            return Text.join(base, appender(args, Text.of(GRAY, ", ", GREEN)));
        }

        try {
            Text.Builder builder = Text.builder();
            String[] split = translation.split("\\{\\}"); // The var that we are replacing {}
            boolean arg = false;
            int countArg = 0;
            int countSplit = 0;

            for (int i = 0 ; i < split.length + args.length; i++) {
                // Part of the message create text and join
                if (arg = !arg) {
                    builder.append(function(split[countSplit++])).build();
                }
                else {
                    builder.append(function(args[countArg++])).build();
                }
            }
            return builder.build();
        }
        catch (MissingFormatArgumentException | ArrayIndexOutOfBoundsException error) {
            return FORMATTING_CODE.deserialize(translation);
        }
    }

    /** Convert the Object to a text object while formatting color codes */
    private Text function(Object object) {
        return object instanceof Text ? (Text) object : FORMATTING_CODE.deserialize(object.toString());
    }

    /** Append Object array to a Text object */
    private Text appender(Object... args) {
        return appender(args, null);
    }

    /** Append Object array to a Text object */
    private Text appender(Object[] args, Text separator) {
        Text.Builder builder = Text.builder();

        for (Object part : args) {
            if (separator != null && !builder.equals(Text.builder())) {
                builder.append(separator);
            }

            builder.append(function(part));
        }

        return builder.build();
    }

    public Locale getLocale() {
        return this.locale;
    }
}
