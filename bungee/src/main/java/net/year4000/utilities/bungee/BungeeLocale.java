package net.year4000.utilities.bungee;

import net.year4000.utilities.locale.LocaleWrapper;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@SuppressWarnings("unused")
public abstract class BungeeLocale extends LocaleWrapper {
    /** Start creating locales for the specific player's locale */
    public BungeeLocale(ProxiedPlayer player) {
        this.locale = player == null ? DEFAULT_LOCALE : player.getLocale().toString();
    }

    /** Translate to the specific locale with formatting */
    public String get(String key, Object... args) {
        return super.get(key, args);
    }
}
