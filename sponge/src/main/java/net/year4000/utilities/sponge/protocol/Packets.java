package net.year4000.utilities.sponge.protocol;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;


public interface Packets {
    /** Get the default PacketManager for this interface */
    static Packets manager() {
        return new PacketManager();
    }

    /** Send a selected packet to the player */
    void sendPacket(Player player, Object packet);

    /** Send the packet to all the players */
    default void sendPacket(Object packet) {
        Sponge.getServer().getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }
}
