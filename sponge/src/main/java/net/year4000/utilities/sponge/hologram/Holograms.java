package net.year4000.utilities.sponge.hologram;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;

import javax.imageio.stream.ImageInputStream;

/** Create holograms */
public interface Holograms {
    /** Get the default hologram manager that is using utilities as the plugin */
    static Holograms manager() {
        return manager(Utilities.get());
    }

    /** Get a hologram manager that is registered with the plugin */
    static Holograms manager(Object plugin) {
        return HologramManager.get(plugin);
    }

    /** Add a text hologram at the specific location */
    default Hologram add(Location<World> location, Text... texts) {
        return add(Sponge.getServer().getOnlinePlayers(), location, texts);
    }

    /** Add a text hologram for the player at the location */
    default Hologram add(Player player, Location<World> location, Text... texts) {
        return add(ImmutableList.of(player), location, texts);
    }

    /** Add a text hologram for the collection of players at the location */
    Hologram add(Collection<Player> players, Location<World> location, Text... texts);

    /** Add a hologram from the image at the specific location */
    default Hologram add(Location<World> location, ImageInputStream image) {
        return add(Sponge.getServer().getOnlinePlayers(), location, image);
    }

    /** Add a image hologram for the player at the location */
    default Hologram add(Player player, Location<World> location, ImageInputStream image) {
        return add(ImmutableList.of(player), location, image);
    }

    /** Add a image hologram for the set of players at the location */
    default Hologram add(Location<World> location, ImageInputStream image, Player... players) {
        return add(ImmutableSet.copyOf(players), location, image);
    }

    /** Add a image hologram for the collection of players at the location */
    Hologram add(Collection<Player> players, Location<World> location, ImageInputStream image);

    /** Remove a hologram from the players */
    default void remove(Player player, Hologram hologram) {
        remove(ImmutableList.of(player), hologram);
    }

    /** Remove a hologram from all players */
    default void remove(Hologram hologram) {
        remove(Sponge.getServer().getOnlinePlayers(), hologram);
    }

    /** Remove a hologram from the set of players */
    default void remove(Hologram hologram, Player... players) {
        remove(ImmutableSet.copyOf(players), hologram);
    }

    /** Remove a hologram from the collection of players */
    default void remove(Collection<Player> players, Hologram hologram) {
        players.forEach(hologram::destroy);
    }

    /** Send a hologram to the specific player */
    default void send(Player player, Hologram hologram) {
        hologram.send(player);
    }

    /** Send a hologram to the set of players */
    default void send(Hologram hologram, Player... players) {
        send(ImmutableSet.copyOf(players), hologram);
    }

    /** Send a hologram to all players */
    default void send(Hologram hologram) {
        send(Sponge.getServer().getOnlinePlayers(), hologram);
    }

    /** Send a hologram to the collection of players */
    default void send(Collection<Player> players, Hologram hologram) {
        players.forEach(hologram::send);
    }
}
