package net.year4000.utilities.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.year4000.utilities.AbstractBadgeManager;
import net.year4000.utilities.AccountBadgeManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BadgeManager extends AbstractBadgeManager<ProxiedPlayer> {
    private final AccountBadgeManager manager = new AccountBadgeManager();

    /** Find the badge the player should have */
    public Badges findBadge(ProxiedPlayer player) {
        return manager.findBadge(player.getUniqueId().toString());
    }

    /** Get the badge in bracket form */
    public String getBadge(ProxiedPlayer player) {
        Badges badge = findBadge(player);
        return MessageUtil.replaceColors("&f[" + badge + "&f]");
    }
}