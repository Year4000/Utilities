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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MessageUtil extends net.year4000.utilities.mc.MessageUtil {
    private MessageUtil() {}

    /**
     * Creates a message with colors and allowed for formatted.
     * @param message The message to create.
     * @param args The optional args to format the message.
     * @return The message all prettied and formatted.
     */
    public static String message(String message, Object... args) {
        return replaceColors(String.format(message, args));
    }

    /**
     * Sends out a raw broadcast message.
     * @param message The message to broadcast.
     */
    public static void broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    /**
     * Sends out a color broadcast message.
     * @param message The message to broadcast.
     */
    public static void colorBroadcast(String message) {
        broadcast(replaceColors(message));
    }

    /**
     * Sends out a raw broadcast message.
     * @param message The message to broadcast.
     */
    public static void rawBroadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendRawMessage(message);
        }
    }
}
