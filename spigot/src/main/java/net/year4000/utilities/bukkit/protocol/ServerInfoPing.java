package net.year4000.utilities.bukkit.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketOutputHandler;
import com.comphenix.protocol.utility.StreamSerializer;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.year4000.utilities.bukkit.Utilities;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Map;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allow sending extra json data with the ping response.
 * Due note that in order to get this to work we are sending
 * the packet id in the netty header. The MC client respects this
 * but other custom server ping do not and need to have this
 * support added.
 */
public class ServerInfoPing implements PacketOutputHandler {
    private static final int MAX_SIZE = 32767;
    private final ListenerPriority priority;
    private final Map<String, Supplier<JsonElement>> customElements = Maps.newConcurrentMap();

    /** Register this ping with specific priority */
    public ServerInfoPing(ListenerPriority priority) {
        this.priority = checkNotNull(priority);
    }

    /** Register this object with normal priority */
    public ServerInfoPing() {
        this(ListenerPriority.NORMAL);
    }

    /** Inject this class into the system */
    public static ServerInfoPing inject(ProtocolManager protocol, ServerInfoPing ping) {
        protocol.addPacketListener(
            new PacketAdapter(ping.getPlugin(), PacketType.Status.Server.OUT_SERVER_INFO) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    event.getNetworkMarker().addOutputHandler(ping);
                }
            }
        );

        return ping;
    }

    @Override
    public Plugin getPlugin() {
        return Utilities.getInst();
    }

    @Override
    public byte[] handle(PacketEvent event, byte[] buffer) {
        Gson gson = new Gson();
        StreamSerializer serializer = event.getNetworkMarker().getSerializer();

        try (
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(buffer));
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytesOut);
        ) {
            // There is a var int header
            int head = serializer.deserializeVarInt(data);
            String json = serializer.deserializeString(data, MAX_SIZE);
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

            // Add the custom elements into the json ping only if there is not one
            synchronized (customElements) {
                customElements.forEach((key, value) -> {
                    if (!jsonObject.has(key)) {
                        jsonObject.add(key, value.get());
                    }
                });
            }

            // Write header and string
            serializer.serializeVarInt(out, head);
            serializer.serializeString(out, gson.toJson(jsonObject));

            return bytesOut.toByteArray();
        }
        catch (IOException error) {
            // could not add to json ping
            return buffer;
        }
    }

    /** Just an element in json ping data */
    public void put(String key, Supplier<JsonElement> value) {
        customElements.put(key, value);
    }

    public ListenerPriority getPriority() {
        return this.priority;
    }
}
