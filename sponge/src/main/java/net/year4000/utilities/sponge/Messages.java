/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.cache.QuickCache;
import net.year4000.utilities.locale.LocaleKeys;
import net.year4000.utilities.locale.Translatable;
import net.year4000.utilities.locale.URLLocaleManager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import static org.spongepowered.api.text.format.TextColors.*;

public enum Messages implements LocaleKeys<CommandSource> {
    // Locale Header
    LOCALE_CODE,
    LOCALE_NAME,

    // Commands
    CMD_ERROR_PLAYER,

    // Plugins Command
    CMD_PLUGINS,
    CMD_VERSION,

    // Fly Command
    CMD_FLY_MODE,
    CMD_FLY_ON,
    CMD_FLY_OFF,
    ;

    public static final Text SUCCESS = Text.of(GRAY, " [", DARK_GREEN, "!", GRAY, "]", GREEN, " ");
    public static final Text NOTICE = Text.of(GRAY, " [", GOLD, "!", GRAY, "]", YELLOW, " ");
    public static final Text ERROR = Text.of(GRAY, " [", DARK_RED, "!", GRAY, "]", RED, " ");

    @Override
    public Translatable apply(Optional<CommandSource> player) {
        if (player.isPresent()) {
            return new SpongeLocale(Factory.inst.get(), player.get());
        }

        return new SpongeLocale(Factory.inst.get());
    }

    /** The factory to handle Locale Managers */
    public static class Factory extends URLLocaleManager {
        private static QuickCache<Messages.Factory> inst = QuickCache.builder(Messages.Factory.class).build();

        public Factory() {
            super("https://raw.githubusercontent.com/Year4000/Locales/master/utilities/");
        }
    }
}
