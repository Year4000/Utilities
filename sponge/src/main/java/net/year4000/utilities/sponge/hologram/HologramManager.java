package net.year4000.utilities.sponge.hologram;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.sponge.protocol.Packets;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

/**
 * The Hologram Manager that will handle all of the holograms, it uses a FrameBuffer to
 * create the frames for the hologram, if they are animated.
 */
public class HologramManager implements Holograms {
    private static final Map<Class<?>, HologramManager> managers = Maps.newConcurrentMap();
    final Multimap<Hologram, Player> animationViewers = MultimapBuilder.ListMultimapBuilder.treeKeys().hashSetValues().build();
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

    @Override
    public Hologram add(Collection<Player> players, Location<World> location, Text... texts) {
        Hologram hologram = new Hologram(this, location, FrameBuffer.builder().add(texts).build());
        send(players, hologram);
        return hologram;
    }

    @Override
    public Hologram add(Collection<Player> players, Location<World> location, ImageInputStream image) {
        try {
            Hologram hologram = new Hologram(this, location, FrameBuffer.builder().add(ImageIO.read(image)).build());
            send(players, hologram);
            return hologram;
        } catch (IOException exception) {
            throw ErrorReporter.builder(exception)
                .add("Player(s): ", players)
                .add("Location: ", location)
                .buildAndReport(System.err);
        }
    }
}
