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

package net.year4000.utilities.bukkit;

import lombok.Getter;
import net.year4000.utilities.bukkit.bossbar.BarAPIListener;
import net.year4000.utilities.bukkit.bossbar.BossBar;
import org.bukkit.Bukkit;

public class Utilities extends BukkitPlugin {
    @Getter
    private static Utilities inst;

    @Override
    public void onLoad() {
        inst = this;
        Messages.LOCALE_NAME.get(); // Trigger a download from server now so it can cache it
    }

    @Override
    public void onEnable() {
        // Register boss bar
        Bukkit.getPluginManager().registerEvents(new BarAPIListener(), this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                BossBar.handleTeleport(player, player.getLocation());
            });
        }, 20L, 20L);
    }
}
