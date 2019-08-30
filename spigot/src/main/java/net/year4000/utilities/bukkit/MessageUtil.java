/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MessageUtil extends net.year4000.utilities.minecraft.MessageUtil {
    private MessageUtil() {
    }

    /**
     * Creates a message with colors and allowed for formatted.
     *
     * @param message The message to create.
     * @param args    The optional args to format the message.
     * @return The message all prettied and formatted.
     */
    public static String message(String message, Object... args) {
        return replaceColors(String.format(message, args));
    }

    /**
     * Sends out a raw broadcast message.
     *
     * @param message The message to broadcast.
     */
    public static void broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    /**
     * Sends out a color broadcast message.
     *
     * @param message The message to broadcast.
     */
    public static void colorBroadcast(String message) {
        broadcast(replaceColors(message));
    }

    /**
     * Sends out a raw broadcast message.
     *
     * @param message The message to broadcast.
     */
    public static void rawBroadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendRawMessage(message);
        }
    }
}
