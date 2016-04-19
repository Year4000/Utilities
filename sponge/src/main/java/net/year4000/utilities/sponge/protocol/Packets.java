package net.year4000.utilities.sponge.protocol;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;


public interface Packets {
    /** Get the default PacketManager for this interface */
    static Packets manager(Object plugin) {
        return new PacketManager(plugin);
    }

    /** Send a selected packet to the player */
    void sendPacket(Player player, Packet packet);

    /** Send the packet to all the players */
    default void sendPacket(Packet packet) {
        Sponge.getServer().getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    /** Register the consumer for the player packet listener */
    void registerListener(Player player, PacketType packetType, PacketListener consumer);

    /** Register the consumer for all the players the packet listener */
    default void registerListener(PacketType packetType, PacketListener consumer) {
        Sponge.getServer().getOnlinePlayers().forEach(player -> registerListener(player, packetType, consumer));
    }
}
