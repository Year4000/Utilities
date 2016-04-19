package net.year4000.utilities.sponge.protocol;

import org.spongepowered.api.entity.living.player.Player;

import java.util.function.BiFunction;

@FunctionalInterface
public interface PacketListener extends BiFunction<Player, Packet, Boolean> {

    /** Return true to stop the packet */
    @Override
    Boolean apply(Player player, Packet packet);
}
