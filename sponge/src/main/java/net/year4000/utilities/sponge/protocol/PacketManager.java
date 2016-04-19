package net.year4000.utilities.sponge.protocol;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Map;

/** The packet manager that inject packets into the netty pipeline */
public class PacketManager implements Packets {
    public static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("player");
    private final Object plugin;
    private final Map<Class<?>, Map<Player, PacketListener>> listeners = Maps.newConcurrentMap();

    /** Creates the manages and register listeners ect */
    public PacketManager(Object plugin) {
        this.plugin = Conditions.nonNull(plugin, "plugin");
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    /** Does the map contain any listeners*/
    public boolean containsListener(PacketType packetType) {
        Conditions.nonNull(packetType, "packetType");
        return containsListener(PacketTypes.fromType(packetType).getOrThrow("packetType"));
    }

    /** Does the map contain a listener, internal use ignores checks */
    boolean containsListener(Class<?> clazz) {
        return listeners.get(clazz) != null;
    }

    /** Get the listener for the packet and player */
    public PacketListener getListener(PacketType packetType, Player player) {
        Conditions.nonNull(packetType, "packetType");
        Conditions.nonNull(player, "player");
        return getListener(PacketTypes.fromType(packetType).getOrThrow("packetType"), player);
    }

    /** Get the listener for the type and player, internal use ignores checks */
    PacketListener getListener(Class<?> clazz, Player player) {
        return listeners.get(clazz).get(player);
    }

    /** The implementation of sending a custom packet to the player */
    @Override
    public void sendPacket(Player player, Packet packet) {
        Conditions.nonNull(player, "player");
        Conditions.nonNull(packet, "packet");
        ProxyEntityPlayerMP entityPlayer = ProxyEntityPlayerMP.of(player);
        entityPlayer.sendPacket(packet);
    }

    /** The implementation on listing for packets */
    @Override
    public void registerListener(Player player, PacketType packetType, PacketListener consumer) {
        Conditions.nonNull(player, "player");
        Conditions.nonNull(packetType, "packetType");
        Conditions.nonNull(consumer, "consumer");
        Class<?> clazz = PacketTypes.fromType(packetType).getOrThrow();
        listeners.putIfAbsent(clazz, Maps.newConcurrentMap());
        Map<Player, PacketListener> playerListeners = listeners.get(clazz);
        Conditions.condition(playerListeners.get(player) == null, "You can only register a listener once");
        playerListeners.put(player, consumer);
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        ProxyEntityPlayerMP proxy = ProxyEntityPlayerMP.of(event.getTargetEntity());
        Channel channel = proxy.netHandlerPlayServer().networkManager().channel();
        channel.attr(PLAYER_KEY).set(event.getTargetEntity());

        // Inject our own encoder that will transmute our packets
        if (channel.pipeline().get(PipelineHandles.PacketEncoder.NAME) == null) {
            channel.pipeline().addFirst(PipelineHandles.PacketEncoder.NAME, new PipelineHandles.PacketEncoder(this));
        }

        // Inject our bi directional packet interceptor
        if (channel.pipeline().get(PipelineHandles.PacketInterceptor.NAME) == null) {
            channel.pipeline().addFirst(PipelineHandles.PacketInterceptor.NAME, new PipelineHandles.PacketInterceptor(this));
        }
    }
}
