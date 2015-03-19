package net.year4000.utilities.bukkit;

import net.year4000.utilities.AbstractBadgeManager;
import net.year4000.utilities.AccountBadgeManager;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BadgeManager extends AbstractBadgeManager<Player> {
    private final AccountBadgeManager manager = new AccountBadgeManager();

    /** Find the badge the player should have */
    public Badges findBadge(Player player) {
        return manager.findBadge(player.getUniqueId().toString());
    }

    /** Get the badge in bracket form */
    public String getBadge(Player player) {
        Badges badge = findBadge(player);
        return MessageUtil.replaceColors("&f[" + badge + "&f]");
    }
}