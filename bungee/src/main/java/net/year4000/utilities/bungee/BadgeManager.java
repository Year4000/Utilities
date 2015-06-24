/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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