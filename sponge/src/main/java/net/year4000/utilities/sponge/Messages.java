/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.cache.QuickCache;
import net.year4000.utilities.locale.LocaleKeys;
import net.year4000.utilities.locale.LocaleUtil;
import net.year4000.utilities.locale.URLLocaleManager;
import org.spongepowered.api.command.CommandSource;

import java.util.Optional;

public enum Messages implements LocaleKeys<CommandSource> {
    // Locale Header
    LOCALE_CODE,
    LOCALE_NAME,

    // Commands
    CMD_PLUGINS,
    CMD_VERSION,
    ;

    @Override
    public LocaleUtil apply(Optional<CommandSource> player) {
        if (player.isPresent()) {
            return new SpongeLocale(Factory.inst.get(), player.get());
        }

        return new SpongeLocale(Factory.inst.get());
    }

    /** The factory to handle Locale Managers */
    public static class Factory extends URLLocaleManager {
        private static QuickCache<Message.Factory> inst = QuickCache.builder(Message.Factory.class).build();

        public Factory() {
            super("https://raw.githubusercontent.com/Year4000/Locales/master/utilities/");
        }
    }
}
