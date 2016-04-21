package net.year4000.utilities.sponge.hologram;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class HologramManager implements Holograms {

    @Override
    public void add(Location<World> location, Text text) {
        new Hologram(location, text).generate();
    }
}
