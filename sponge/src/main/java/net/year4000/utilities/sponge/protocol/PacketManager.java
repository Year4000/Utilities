package net.year4000.utilities.sponge.protocol;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.util.AttributeKey;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.Utils;
import net.year4000.utilities.scheduler.Scheduler;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/** The packet manager that inject packets into the netty pipeline */
public class PacketManager implements Packets {
    public static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("player");
    public static final AttributeKey<PacketManager> PACKET_MANAGER_KEY = AttributeKey.valueOf("packet_manager");
    private final Scheduler scheduler;
    final UUID id = UUID.randomUUID();
    final Map<Class<?>, PacketListener> listeners = Maps.newConcurrentMap();
    final String plugin;

    /** Creates the manages and register listeners ect */
    public PacketManager(Object plugin) {
        Conditions.nonNull(plugin, "plugin");
        Sponge.getEventManager().registerListeners(plugin, this);
        scheduler = Scheduler.builder().executor(Sponge.getScheduler().createAsyncExecutor(plugin)).build();
        this.plugin = Sponge.getPluginManager().fromInstance(plugin).get().getId();
    }

    /** Used for unit tests */
    PacketManager() {
        scheduler = Scheduler.builder().build();
        plugin = "utilities";
    }

    /** Does the map contain any listeners*/
    @Override
    public boolean containsListener(PacketType packetType) {
        Conditions.nonNull(packetType, "packetType");
        return containsListener(PacketTypes.fromType(packetType).getOrThrow("packetType"));
    }

    /** Does the map contain a listener, internal use ignores checks */
    boolean containsListener(Class<?> clazz) {
        return listeners.get(clazz) != null;
    }

    /** Get the listener for the packet and player */
    @Override
    public PacketListener getListener(PacketType packetType) {
        Conditions.nonNull(packetType, "packetType");
        return getListener(PacketTypes.fromType(packetType).getOrThrow("packetType"));
    }

    /** Get the listener for the type and player, internal use ignores checks */
    PacketListener getListener(Class<?> clazz) {
        return listeners.get(clazz);
    }

    /** Remove the listener for the packet type */
    @Override
    public void removeListener(PacketType packetType) {
        Conditions.nonNull(packetType, "packetType");
        removeListener(PacketTypes.fromType(packetType).getOrThrow("packetType"));
    }

    /** Remove the listener unit test method */
    void removeListener(Class<?> clazz) {
        listeners.remove(clazz);
    }

    /** The implementation of sending a custom packet to the player */
    @Override
    public void sendPacket(Player player, Packet packet) {
        Conditions.nonNull(player, "player");
        Conditions.nonNull(packet, "packet");
        try {
            ProxyEntityPlayerMP.of(player).sendPacket(packet);
        } catch (Throwable throwable) {
            ErrorReporter.builder(throwable)
                .hideStackTrace()
                .add("Player: ", player.getName())
                .add("Packet ID: ", Integer.toHexString(packet.packetType().id()))
                .add("Packet State: ", PacketTypes.State.values()[packet.packetType().state()])
                .add("Packet Bounded: ", PacketTypes.Binding.values()[packet.packetType().bounded()])
                .add("Packet Class: ", packet.mcPacketClass())
                .add("Packet Object: ", packet.mcPacket())
                .buildAndReport(System.err);
        }
    }

    @Override
    public void sendPacket(Player player, Packet packet, long offset, TimeUnit unit) {
        scheduler.run(() -> sendPacket(player, packet), (int) offset, unit);
    }

    @Override
    public void repeatPacket(Player player, Packet packet, long delay, TimeUnit unit) {
        scheduler.repeat(() -> sendPacket(player, packet), (int) delay, unit);
    }

    /** The implementation on listing for packets */
    @Override
    public void registerListener(PacketType packetType, PacketListener consumer) {
        Conditions.nonNull(packetType, "packetType");
        Conditions.nonNull(consumer, "consumer");
        Class<?> clazz = PacketTypes.fromType(packetType).getOrThrow();
        registerListener(clazz, consumer);
    }

    /** Register the listener, used for the unit test */
    void registerListener(Class<?> clazz, PacketListener consumer) {
        listeners.put(clazz, consumer);
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        try {
            ProxyEntityPlayerMP proxy = ProxyEntityPlayerMP.of(event.getTargetEntity());
            Channel channel = proxy.netHandlerPlayServer().networkManager().channel();
            channel.attr(PLAYER_KEY).set(event.getTargetEntity());
            channel.attr(PACKET_MANAGER_KEY).set(this);
            ChannelPipeline pipeline = channel.pipeline();
            if (pipeline.get(PipelineHandles.INBOUND_NAME) == null) {
                String where = (pipeline.get("fml:packet_handler") != null) ? "fml:packet_handler" : "packet_handler";
                pipeline.addBefore(where, PipelineHandles.INBOUND_NAME, PipelineHandles.INBOUND_PACKET_INTERCEPTOR);
            }
            if (pipeline.get(PipelineHandles.OUTBOUND_NAME) == null) {
                pipeline.addAfter("packet_handler", PipelineHandles.OUTBOUND_NAME, PipelineHandles.OUTBOUND_PACKET_INTERCEPTOR);
            }
        } catch (Throwable throwable) {
            ErrorReporter.builder(throwable)
                .hideStackTrace()
                .add("Could not inject the packet interceptor for: ", event.getTargetEntity().getName())
                .buildAndReport(System.err);
        }
    }
}
