package net.year4000.utilities.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.year4000.utilities.AbstractBadgeManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BadgeManager extends AbstractBadgeManager<ProxiedPlayer> {
    /** Find the badge the player should have */
    public Badges findBadge(ProxiedPlayer player) {
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
    public String getBadge(ProxiedPlayer player) {
        Badges badge = findBadge(player);
        return MessageUtil.replaceColors("&f[" + badge + "&f]");
    }
}