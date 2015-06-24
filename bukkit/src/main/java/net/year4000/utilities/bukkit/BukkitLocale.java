package net.year4000.utilities.bukkit;

import net.year4000.utilities.locale.LocaleWrapper;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public abstract class BukkitLocale extends LocaleWrapper {
    /** Start creating locales for the specific player's locale */
    public BukkitLocale(Player player) {
        this.locale = player == null ? DEFAULT_LOCALE : player.spigot().getLocale();
    }

    /** Translate to the specific locale with formatting */
    public String get(String key, Object... args) {
        return super.get(key, args);
    }
}
