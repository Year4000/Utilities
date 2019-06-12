/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.minecraft;

import com.google.common.annotations.Beta;

@SuppressWarnings("unused")
public abstract class MessageUtil {
    /**
     * Replace color codes with Minecraft colors.
     *
     * @param message The message to be translated.
     * @return The translated message.
     */
    public static String replaceColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Strip Minecraft colors to create a clean message.
     *
     * @param message The message to strip from.
     * @return The clean message with no colors.
     */
    public static String stripColors(String message) {
        return ChatColor.stripColor(message);
    }

    /**
     * Replace Minecraft colors with color codes.
     *
     * @param message The message to be translated.
     * @return The translated message.
     */
    public static String replaceRawColors(String message) {
        return message.replaceAll(ChatColor.COLOR_CHAR + "[0-9a-fA-Fk-rK-R]", "&%1");
    }

    /**
     * Check is the message could be a raw message
     *
     * @param message Message to check.
     * @return true|false
     */
    public static boolean isRawMessage(String message) {
        return message.startsWith("{text:") || message.startsWith("{text:", 1);
    }

    /**
     * Replace end of line symbol with a char.
     *
     * @param message The message to be translated.
     * @return The translated message.
     */
    @Beta
    public static String endOfLine(String message) {
        return message.replaceAll("\\n", "\n");
    }
}
