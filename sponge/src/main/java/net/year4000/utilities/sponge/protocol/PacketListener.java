/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.protocol;

import org.spongepowered.api.entity.living.player.Player;

import java.util.function.BiFunction;

@FunctionalInterface
public interface PacketListener extends BiFunction<Player, Packet, Boolean> {
    /** Should the event be ignored, this leaves the packet to be handled normally */
    boolean IGNORE = false;

    /** Should the event be canceled, prevents the server/client ever knowing about it */
    boolean CANCEL = true;

    /** Return true to stop the packet */
    @Override
    Boolean apply(Player player, Packet packet);
}
