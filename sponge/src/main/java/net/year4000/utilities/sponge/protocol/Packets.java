/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.protocol;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface Packets {
    /** Get the default PacketManager for this interface using utilities as the plugin */
    static Packets manager() {
        return manager(Utilities.get());
    }

    /** Get the default PacketManager for this interface using the plugin you supplied */
    static Packets manager(Object plugin) {
        return PacketManager.get(plugin);
    }

    /** Send the packet to the player */
    default void sendPacket(Player player, Packet packet) {
        sendPacket(ImmutableList.of(player), packet);
    }

    /** Send the packet to the set of players */
    default void sendPacket(Packet packet, Player... players) {
        sendPacket(ImmutableSet.copyOf(players), packet);
    }

    /** Send the packet to all the online players */
    default void sendPacket(Packet packet) {
        sendPacket(Sponge.getServer().getOnlinePlayers(), packet);
    }

    /** Send the packet to the collection of players */
    void sendPacket(Collection<Player> players, Packet packet);

    /** Send the packet with the offset to the player */
    default void sendPacket(Player player, Packet packet, long offset, TimeUnit unit) {
        sendPacket(ImmutableList.of(player), packet, offset, unit);
    }

    /** Send the packet with the offset to the set of players */
    default void sendPacket(Packet packet, long offset, TimeUnit unit, Player... players) {
        sendPacket(ImmutableSet.copyOf(players), packet, offset, unit);
    }

    /** Send the packet with the offset to all the online players */
    default void sendPacket(Packet packet, long offset, TimeUnit unit) {
        sendPacket(Sponge.getServer().getOnlinePlayers(), packet, offset, unit);
    }

    /** Send the packet with the offset to the collection of players */
    void sendPacket(Collection<Player> players, Packet packet, long offset, TimeUnit unit);

    /** Repeat the packet with the delay to the player */
    default void repeatPacket(Player player, Packet packet, long delay, TimeUnit unit) {
        repeatPacket(ImmutableList.of(player), packet, delay, unit);
    }

    /** Repeat the packet with the delay to the set of players */
    default void repeatPacket(Packet packet, long delay, TimeUnit unit, Player... players) {
        repeatPacket(ImmutableSet.copyOf(players), packet, delay, unit);
    }

    /** Repeat the packet with the delay to all the players */
    default void repeatPacket(Packet packet, long offset, TimeUnit unit) {
        repeatPacket(Sponge.getServer().getOnlinePlayers(), packet, offset, unit);
    }

    /** Repeat the packet with the delay to the collection of players */
    void repeatPacket(Collection<Player> players, Packet packet, long delay, TimeUnit unit);

    /** Register the consumer for all the players the packet listener */
    void registerListener(PacketType packetType, PacketListener consumer);

    /** Is the manager listening to the current packet type */
    boolean containsListener(PacketType packetType);

    /** Get the listener for the packet type */
    PacketListener getListener(PacketType packetType);

    /** Remove the listener for the packet type */
    void removeListener(PacketType packetType);
}
