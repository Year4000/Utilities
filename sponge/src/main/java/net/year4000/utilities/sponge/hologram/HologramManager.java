package net.year4000.utilities.sponge.hologram;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.sponge.protocol.Packets;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class HologramManager implements Holograms {
    final Object plugin;
    final Packets packets;

    HologramManager(Object plugin) {
        this.plugin = Conditions.nonNull(plugin, "plugin");
        this.packets = Packets.manager(plugin);
    }

    @Override
    public void add(Location<World> location, Text... text) {
        new Hologram(this, location, text).generate();
    }
}
