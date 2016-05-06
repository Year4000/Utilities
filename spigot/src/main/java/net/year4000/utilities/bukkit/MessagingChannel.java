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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.year4000.utilities.Callback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MessagingChannel implements PluginMessageListener {
    private static final String CHANNEL = "BungeeCord";
    private static final String FORWARD = "Forward";
    private static final String FORWARD_TO_PLAYER = "ForwardToPlayer";
    private static MessagingChannel inst;
    private final Deque<AbstractMap.Entry<String, Callback<ByteArrayDataInput>>> requests = new ArrayDeque<>();
    private final ConcurrentMap<String, Callback<ByteArrayDataInput>> custom = new ConcurrentHashMap<>();

    private MessagingChannel() {
    }

    /** Get the messaging channel instance */
    public static MessagingChannel get() {
        if (inst == null) {
            inst = new MessagingChannel();
            inst.register();
        }
        return inst;
    }

    /** Register plugin channels */
    private void register() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(Utilities.getInst(), CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(Utilities.getInst(), CHANNEL, this);
    }

    /** Add custom callbacks for forwarded channels */
    public void addForwardCallback(String id, Callback<ByteArrayDataInput> callback) {
        custom.putIfAbsent(id, callback);
    }

    /** Send data and return a callback */
    public void send(String[] data, Callback<ByteArrayDataInput> back) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Utilities.debug("No players online can not send data");
            return;
        }

        Player player = Bukkit.getOnlinePlayers().iterator().next();
        sendToPlayer(player, data, back);
    }

    /** Send data to a player and return a callback */
    public void sendToPlayer(Player player, String[] data, Callback<ByteArrayDataInput> back) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        for (String line : data) {
            out.writeUTF(line);
        }

        player.sendPluginMessage(Utilities.getInst(), CHANNEL, out.toByteArray());
        requests.add(new AbstractMap.SimpleImmutableEntry<>(data[0], back));
    }

    /** Send data with out needing an response */
    public void send(String[] data) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Utilities.debug("No players online can not send data");
            return;
        }

        Player player = Bukkit.getOnlinePlayers().iterator().next();
        sendToPlayer(player, data);
    }

    /** Send data to a player with out needing an response */
    public void sendToPlayer(Player player, String[] data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        for (String line : data) {
            out.writeUTF(line);
        }

        player.sendPluginMessage(Utilities.getInst(), CHANNEL, out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals(CHANNEL)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();
        Utilities.debug("INCOMING CHANNEL: " + subChannel);

        if (subChannel.equals(FORWARD) || subChannel.equals(FORWARD_TO_PLAYER)) {
            String customChannel = in.readUTF();
            Utilities.debug("FORWARD CHANNEL: " + customChannel);
            Utilities.debug("FORWARD DATA: " + in);

            if (custom.containsKey(customChannel)) {
                custom.get(customChannel).callback(in);
            }
        }
        else {
            Utilities.debug("SUB REQUESTS: " + requests.size());
            Utilities.debug("SUB DATA: " + in);

            if (requests.size() > 0 && subChannel.equals(requests.peek().getKey())) {
                requests.poll().getValue().callback(in);
            }
        }
    }
}
