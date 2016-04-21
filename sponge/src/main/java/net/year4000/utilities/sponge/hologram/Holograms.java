package net.year4000.utilities.sponge.hologram;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Holograms {
    static Holograms manager() {
        return new HologramManager();
    }

    void add(Location<World> location, Text text);
}
