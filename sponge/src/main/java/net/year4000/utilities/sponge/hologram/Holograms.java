package net.year4000.utilities.sponge.hologram;

import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.awt.image.BufferedImage;

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

    Hologram add(Location<World> location, Text... text);

    Hologram add(Location<World> location, BufferedImage image);
}
