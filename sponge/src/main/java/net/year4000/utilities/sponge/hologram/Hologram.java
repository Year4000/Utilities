package net.year4000.utilities.sponge.hologram;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.Lists;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Hologram {
    private List<Text> lines;
    private Vector3d vector3d;
    private Extent extent;
    private transient List<ArmorStand> armorStands = Lists.newArrayList();

    Hologram(Location<World> location, Text... lines) {
        Conditions.nonNull(location, "location");
        this.vector3d = location.getPosition();
        this.extent = location.getExtent();
        this.lines = Arrays.asList(Conditions.nonNull(lines, "lines"));
    }

    public void generate() {
        for (Text line : lines) {
            ArmorStand entity = line(0, line);
            if (entity != null) {
                extent.spawnEntity(entity, Cause.source(Utilities.get()).build());
                armorStands.add(entity);
            }
        }
    }

    private ArmorStand line(double offset, Text text) {
        Optional<Entity> entityOptional = extent.createEntity(EntityTypes.ARMOR_STAND, vector3d);
        if (entityOptional.isPresent()) {
            ArmorStand armorStand = (ArmorStand) entityOptional.get();
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
            armorStand.offer(Keys.DISPLAY_NAME, text);
            armorStand.offer(Keys.INVISIBLE, true);
            return armorStand;
        }
        return null;
    }
}
