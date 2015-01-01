package net.year4000.utilities.bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.year4000.utilities.Callback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
@NoArgsConstructor
public final class MessagingChannel implements PluginMessageListener {
    private static final String CHANNEL = "BungeeCord";
    private static final String FORWARD = "Forward";
    private final Deque<AbstractMap.Entry<String, Callback<ByteArrayDataInput>>> requests = new ArrayDeque<>();
    private final ConcurrentMap<String, Callback<ByteArrayDataInput>> custom = new ConcurrentHashMap<>();
    private Plugin plugin = null;

    private Plugin getPlugin() {
        return plugin == null ? getPlugin() : plugin;
    }

    /** Register plugin channels */
    public void register() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(getPlugin(), CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(getPlugin(), CHANNEL, this);
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

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        for (String line : data) {
            out.writeUTF(line);
        }

        Player player = Bukkit.getOnlinePlayers().iterator().next();
        player.sendPluginMessage(getPlugin(), CHANNEL, out.toByteArray());
        requests.add(new AbstractMap.SimpleImmutableEntry<>(data[0], back));
    }

    /** Send data with out needing an response */
    public void send(String[] data) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Utilities.debug("No players online can not send data");
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        for (String line : data) {
            out.writeUTF(line);
        }

        Player player = Bukkit.getOnlinePlayers().iterator().next();
        player.sendPluginMessage(getPlugin(), CHANNEL, out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals(CHANNEL))  return;

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();

        if (subChannel.equals(FORWARD)) {
            String customChannel = in.readUTF();

            if (custom.containsKey(customChannel)) {
                custom.get(customChannel).callback(in);
            }
        }
        else {
            if (requests.size() > 0 && subChannel.equals(requests.peek().getKey())) {
                requests.poll().getValue().callback(in);
            }
        }
    }
}
