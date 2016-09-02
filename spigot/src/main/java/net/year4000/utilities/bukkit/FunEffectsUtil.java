/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit;

import org.bukkit.*;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
/** Fun effects and a simple function. */
public final class FunEffectsUtil {
    private FunEffectsUtil() {
    }

    /** Player a sound for the player. */
    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 100F, 100F);
    }

    /** Play a note for the player. */
    public static void playSound(Player player, Instrument instrument, Note note) {
        player.playNote(player.getLocation(), instrument, note);
    }

    /** Play an effect for the player. */
    public static void playEffect(Player player, EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    /** Play an effect for the player. */
    public static void playEffect(Player player, Effect effect) {
        player.playEffect(player.getLocation(), effect, 5);
    }
}
