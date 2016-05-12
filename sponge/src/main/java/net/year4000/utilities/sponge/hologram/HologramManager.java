package net.year4000.utilities.sponge.hologram;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Maps;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.sponge.protocol.Packets;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Map;

/**
 * The Hologram Manager that will handle all of the holograms, it uses a FrameBuffer to
 * create the frames for the hologram, if they are animated.
 */
public class HologramManager implements Holograms {
    private static final Map<Class<?>, HologramManager> managers = Maps.newConcurrentMap();
    final Object plugin;
    final Packets packets;

    protected HologramManager(Object plugin) {
        this.plugin = Conditions.nonNull(plugin, "plugin");
        this.packets = Packets.manager(plugin);
    }

    /** Only one HologramManager per instance from Packets.manager() */
    public static HologramManager get(Object plugin) {
        Class<?> clazz = plugin.getClass();
        managers.putIfAbsent(clazz, new HologramManager(plugin));
        return managers.get(clazz);
    }

    @Override public Hologram add(Location<World> location, Text... text) {
        return null;
    }

    @Override public Hologram add(Collection<Player> players, Vector3d vector, Text... text) {
        return null;
    }

    @Override public Hologram add(Location<World> location, BufferedImage image) {
        return null;
    }

    @Override public Hologram add(Collection<Player> players, Vector3d vector, BufferedImage image) {
        return null;
    }

    @Override public void remove(Player player, Hologram hologram) {

    }
}
