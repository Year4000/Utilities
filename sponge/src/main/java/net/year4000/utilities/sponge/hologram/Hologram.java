package net.year4000.utilities.sponge.hologram;

import static net.year4000.utilities.sponge.protocol.PacketTypes.Binding.OUTBOUND;
import static net.year4000.utilities.sponge.protocol.PacketTypes.State.PLAY;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Throwables;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Reflections;
import net.year4000.utilities.sponge.protocol.Packet;
import net.year4000.utilities.sponge.protocol.PacketTypes;
import net.year4000.utilities.value.Value;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Hologram {
    private static final double OFFSET = 0.25;
    private final HologramManager manager;
    private final List<Text> lines;
    private final Vector3d origin;
    private final Extent extent;

    Hologram(HologramManager manager, Location<World> location, Text... lines) {
        this.manager = Conditions.nonNull(manager, "manager");
        Conditions.nonNull(location, "location");
        this.origin = location.getPosition();
        this.extent = location.getExtent();
        this.lines = Arrays.asList(Conditions.nonNull(lines, "lines"));
    }

    /** Send the packets */
    public void generate() {
        double y = (lines.size() / 2) * OFFSET; // Shift origin so hologram's origin is in the center
        for (Text line : lines) {
            line(y += OFFSET, line).ifPresent(entity -> {
                manager.packets.sendPacket(new Packet(PacketTypes.of(PLAY, OUTBOUND, 0x0F)).inject(clazz -> {
                    try {
                        Class<?> entityClass = Reflections.clazz("net.minecraft.entity.EntityLivingBase").get();
                        return clazz.getConstructor(entityClass).newInstance(entity);
                    } catch (Exception error) {
                        throw Throwables.propagate(error);
                    }
                }));
            });
        }
    }

    /** Create the armor stand entity */
    private Value<ArmorStand> line(double offset, Text text) {
        Optional<Entity> entityOptional = extent.createEntity(EntityTypes.ARMOR_STAND, origin.sub(0, offset, 0));
        if (entityOptional.isPresent()) {
            ArmorStand armorStand = (ArmorStand) entityOptional.get();
            armorStand.offer(Keys.ARMOR_STAND_HAS_GRAVITY, false);
            armorStand.offer(Keys.ARMOR_STAND_IS_SMALL, true);
            armorStand.offer(Keys.CUSTOM_NAME_VISIBLE, true);
            armorStand.offer(Keys.DISPLAY_NAME, text);
            armorStand.offer(Keys.INVISIBLE, true);
            return Value.of(armorStand);
        }
        return Value.empty();
    }
}
