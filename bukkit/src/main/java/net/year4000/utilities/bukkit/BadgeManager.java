package net.year4000.utilities.bukkit;

import net.year4000.utilities.AbstractBadgeManager;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BadgeManager extends AbstractBadgeManager<Player> {
    /** Find the badge the player should have */
    public Badges findBadge(Player player) {
        List<Badges> ranks = Arrays.asList(Badges.values());
        Collections.reverse(ranks);

        for (Badges badge : ranks) {
            if (player.hasPermission(badge.getPermission())) {
                return badge;
            }
        }

        return Badges.ALPHA;
    }

    /** Get the badge in bracket form */
    public String getBadge(Player player) {
        Badges badge = findBadge(player);
        return MessageUtil.replaceColors("&f[" + badge + "&f]");
    }
}