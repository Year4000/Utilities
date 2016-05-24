/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.year4000.utilities.sdk.AbstractBadgeManager;
import net.year4000.utilities.sdk.AccountBadgeManager;

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