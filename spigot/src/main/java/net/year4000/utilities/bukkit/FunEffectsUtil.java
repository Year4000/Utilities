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

import org.bukkit.*;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
/** Fun effects and a simple function. */
public final class FunEffectsUtil {
    private FunEffectsUtil() {
    }

    /** Player a sound for the player. */
    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 100F, 100F);
    }

    /** Play a note for the player. */
    public static void playSound(Player player, Instrument instrument, Note note) {
        player.playNote(player.getLocation(), instrument, note);
    }

    /** Play an effect for the player. */
    public static void playEffect(Player player, EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    /** Play an effect for the player. */
    public static void playEffect(Player player, Effect effect) {
        player.playEffect(player.getLocation(), effect, 5);
    }
}
