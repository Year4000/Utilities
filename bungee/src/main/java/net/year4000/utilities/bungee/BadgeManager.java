package net.year4000.utilities.bungee;

import net.year4000.utilities.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BadgeManager extends AbstractBadgeManager<Player> {
    public static final int MAX_RANK = Badges.values().length;

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
        return net.year4000.utilities.MessageUtil.replaceColors("&f[" + badge + "&f]");
    }
}