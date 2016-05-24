/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit;

import net.year4000.utilities.sdk.AbstractBadgeManager;
import net.year4000.utilities.sdk.AccountBadgeManager;
import org.bukkit.entity.Player;

public final class BadgeManager extends AbstractBadgeManager<Player> {
    private final AccountBadgeManager manager = new AccountBadgeManager();

    /** Find the badge the player should have */
    public AbstractBadgeManager.Badges findBadge(Player player) {
        return manager.findBadge(player.getUniqueId().toString());
    }

    /** Get the badge in bracket form */
    public String getBadge(Player player) {
        Badges badge = findBadge(player);
        return MessageUtil.replaceColors("&f[" + badge + "&f]");
    }
}