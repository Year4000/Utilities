package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public interface Packets {
    /** Get the default PacketManager for this interface using utilities as the plugin */
    static Packets manager() {
        return manager(Utilities.get());
    }

    /** Get the default PacketManager for this interface using the plugin you supplied */
    static Packets manager(Object plugin) {
        return new PacketManager(plugin);
    }

    /** Send a selected packet to the player */
    void sendPacket(Player player, Packet packet);

    /** Send the packet to all the players */
    default void sendPacket(Packet packet) {
        Sponge.getServer().getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    /** Send the packet with the offset */
    void sendPacket(Player player, Packet packet, long offset, TimeUnit unit);

    /** Send the packet to all the players */
    default void sendPacket(Packet packet, long offset, TimeUnit unit) {
        Sponge.getServer().getOnlinePlayers().forEach(player -> sendPacket(player, packet, offset, unit));
    }

    /** Repeat the packet with the delay */
    void repeatPacket(Player player, Packet packet, long delay, TimeUnit unit);

    /** Send the packet to all the players */
    default void repeatPacket(Packet packet, long offset, TimeUnit unit) {
        Sponge.getServer().getOnlinePlayers().forEach(player -> repeatPacket(player, packet, offset, unit));
    }

    /** Register the consumer for all the players the packet listener */
    void registerListener(PacketType packetType, PacketListener consumer);

    /** Is the manager listening to the current packet type */
    boolean containsListener(PacketType packetType);

    /** Get the listener for the packet type */
    PacketListener getListener(PacketType packetType);

    /** Remove the listener for the packet type */
    void removeListener(PacketType packetType);
}
