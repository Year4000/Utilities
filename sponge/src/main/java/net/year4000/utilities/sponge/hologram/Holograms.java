package net.year4000.utilities.sponge.hologram;

import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Holograms {
    static Holograms manager(Object instance) {
        return new HologramManager(instance);
    }

    static Holograms manager() {
        return manager(Utilities.get());
    }

    void add(Location<World> location, Text... text);
}
