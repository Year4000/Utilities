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

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

@SuppressWarnings("unused")
public final class MessageUtil extends net.year4000.utilities.MessageUtil {
    private MessageUtil() {}

    /**
     * Creates a message with colors and allowed for formatted.
     * @param message The message to create.
     * @param args The optional args to format the message.
     * @return The message all prettied and formatted.
     */
    public static BaseComponent[] message(String message, Object... args) {
        return makeMessage(replaceColors(String.format(message, args)));
    }

    /**
     * Sends out a broadcast message.
     * @param message The message to broadcast.
     */
    public static void broadcast(String message, Object... args) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(message(message, args));
        }
    }

    /**
     * Sends out a broadcast message.
     * @param message The message to broadcast.
     */
    public static void broadcast(BaseComponent[] message) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
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
     * Translate a normal string to a BaseComponent.
     * @param message The message to translate.
     * @return The translated message.
     */
    public static BaseComponent[] makeMessage(String message) {
        return TextComponent.fromLegacyText(message);
    }

    /**
     * Parse a message to be used.
     * @param message The message.
     * @return The parsed message.
     * @throws java.lang.Exception
     */
    public static BaseComponent[] parseMessage(String message) throws Exception {
        try {
            // Raw Message
            if (message.startsWith("{text:") || message.startsWith("{text:", 1)) {
                return ComponentSerializer.parse(message);
            }
            else {
                throw new Exception("Not a raw message.");
            }
        } catch (Exception e) {
            throw new Exception("Message could not be parsed.");
        }
    }

    /**
     * Merge two BaseComponents together.
     * @param first The first BaseComponent
     * @param second The second BaseComponet
     * @return The merged
     */
    public static BaseComponent[] merge(BaseComponent[] first, BaseComponent[] second) {
        BaseComponent[] merged = new BaseComponent[first.length + second.length];

        // Merge the components.
        for (int i = 0; i < first.length + second.length; i++) {
            merged[i] = i < first.length ? first[i] : second[i - first.length];
        }

        return merged;
    }

    /**
     * Merge two BaseComponents together.
     * @param first The first String
     * @param second The second String
     * @return The merged
     */
    public static BaseComponent[] merge(String first, String second) {
        return merge(makeMessage(first), message(second));
    }

    /**
     * Merge two BaseComponents together.
     * @param first The first String
     * @param second The second String
     * @return The merged
     */
    public static BaseComponent[] merge(BaseComponent[] first, String second) {
        return merge(first, message(second));
    }

    /**
     * Merge two BaseComponents together.
     * @param first The first String
     * @param second The second String
     * @return The merged
     */
    public static BaseComponent[] merge(String first, BaseComponent[] second) {
        return merge(message(first), second);
    }
}
