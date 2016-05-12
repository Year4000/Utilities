package net.year4000.utilities.sponge.hologram;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.awt.image.BufferedImage;
import java.util.Collection;

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
    Hologram add(Location<World> location, Text... text);

    /** Add a text hologram for the player at the location */
    default Hologram add(Player player, Vector3d vector, Text... text) {
        return add(Lists.newArrayList(player), vector, text);
    }

    /** Add a text hologram for the collection of players at the location */
    Hologram add(Collection<Player> players, Vector3d vector, Text... text);

    /** Add a hologram from the image at the specific location */
    Hologram add(Location<World> location, BufferedImage image);

    /** Add a image hologram for the player at the location */
    default Hologram add(Player player, Vector3d vector, BufferedImage image) {
        return add(Lists.newArrayList(player), vector, image);
    }

    /** Add a image hologram for the set of players at the location */
    default Hologram add(Vector3d vector, BufferedImage image, Player... players) {
        return add(Sets.newHashSet(players), vector, image);
    }

    /** Add a image hologram for the collection of players at the location */
    Hologram add(Collection<Player> players, Vector3d vector, BufferedImage image);

    /** Remove a hologram from the players */
    void remove(Player player, Hologram hologram);

    /** Remove a hologram from all players */
    default void remove(Hologram hologram) {
        Sponge.getServer().getOnlinePlayers().forEach(player -> remove(player, hologram));
    }
}
