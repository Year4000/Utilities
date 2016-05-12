package net.year4000.utilities.sponge.hologram;

import com.google.common.collect.Maps;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.sponge.protocol.Packets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.awt.image.BufferedImage;
import java.util.Map;

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

    @Override
    public Hologram add(Location<World> location, Text... text) {
        Hologram hologram = new Hologram(this, location, FrameBuffer.builder().add(text).build());
        Sponge.getServer().getOnlinePlayers().forEach(hologram::send);
        return hologram;
    }

    @Override
    public Hologram add(Location<World> location, BufferedImage image) {
        Hologram hologram = new Hologram(this, location, FrameBuffer.builder().add(image).build());
        Sponge.getServer().getOnlinePlayers().forEach(hologram::send);
        return hologram;
    }

    @Override
    public void remove(Hologram hologram) {
        Sponge.getServer().getOnlinePlayers().forEach(hologram::destroy);
    }
}
