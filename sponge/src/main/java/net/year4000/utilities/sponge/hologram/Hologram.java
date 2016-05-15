package net.year4000.utilities.sponge.hologram;

import static net.year4000.utilities.sponge.protocol.PacketTypes.V1_8.PLAY_CLIENT_DESTROY_ENTITIES;
import static net.year4000.utilities.sponge.protocol.PacketTypes.V1_8.PLAY_CLIENT_ENTITY_METADATA;
import static net.year4000.utilities.sponge.protocol.PacketTypes.V1_8.PLAY_CLIENT_SPAWN_MOB;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Reflections;
import net.year4000.utilities.sponge.protocol.Packet;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEntity;
import net.year4000.utilities.value.Value;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * A light weight and fast system to get the holograms displayed.
 * This class will be returned from Holograms but must be passed through
 * the manager to do anything with it. It is only exposed to allow
 * the hologram to be stored as a reference somewhere else.
 */
public class Hologram implements Comparable<Hologram> {
    private static final double OFFSET = 0.25;
    private final HologramManager manager;
    private final Vector3d origin;
    private final Extent extent;
    private final List<Packet> spawnPackets;
    private final List<ArmorStand> armorStands;
    private final ReadWriteLock viewerLock = new ReentrantReadWriteLock();
    private List<WeakReference<Player>> viewers = Lists.newArrayList();
    private Packet destroyPacket;
    private FrameBuffer buffer;
    private boolean changed;

    /** Create the hologram at the location and with the buffer*/
    Hologram(HologramManager manager, Location<World> location, FrameBuffer buffer) {
        this.manager = Conditions.nonNull(manager, "manager");
        Conditions.nonNull(location, "location");
        this.origin = location.getPosition();
        this.extent = location.getExtent();
        this.buffer = Conditions.nonNull(buffer, "buffer");
        this.spawnPackets = Lists.newArrayListWithCapacity(buffer.size());
        this.armorStands = Lists.newArrayListWithCapacity(buffer.size());
    }

    /** Send the hologram to the player */
    void send(Player player) {
        if (changed || spawnPackets.size() == 0) {
            generate(); // Only create the packets once we need to send them
            changed = false;
        }
        try {
            viewerLock.writeLock().lock();
            viewers.add(new WeakReference<>(player));
        } finally {
            viewerLock.writeLock().unlock();
            spawnPackets.forEach(packet -> manager.packets.sendPacket(player, packet));
        }
    }

    /** Destroy the hologram for the player */
    void destroy(Player player) {
        if (spawnPackets.size() > 0 && destroyPacket != null) {
            manager.packets.sendPacket(player, destroyPacket);
        }
        try {
            viewerLock.writeLock().lock();
            // Cleans up the viewers and removes the player from the list
            this.viewers = this.viewers.stream()
                .filter(weakPlayer -> weakPlayer.get() != null && !weakPlayer.get().equals(player))
                .collect(Collectors.toList());
        } finally {
            viewerLock.writeLock().unlock();
        }
    }

    /** Update the hologram with the framebuffer */
    void update(Player player, FrameBuffer buffer) {
        if (armorStands.size() > 0) {
            List<Packet> update = Lists.newArrayList();
            int size = Math.min(buffer.size(), armorStands.size());
            for (int i = 0 ; i < size; i++) {
                Entity entity = armorStands.get(i);
                entity.offer(Keys.DISPLAY_NAME, buffer.get(i));
                update.add(new Packet(PLAY_CLIENT_ENTITY_METADATA).injector()
                    .add(entity.hashCode())
                    .add(ProxyEntity.of(entity).dataWatcher().watching())
                    .inject());
            }
            update.forEach(packet -> manager.packets.sendPacket(player, packet));
            this.buffer = buffer;
            changed = true;
        }
    }

    /** Get the viewers of the hologram, then tries to clean up the viewers */
    Collection<Player> viewers() {
        try {
            viewerLock.readLock().lock();
            ImmutableSet.Builder<Player> viewers = ImmutableSet.builder();
            this.viewers.stream().map(WeakReference::get).forEach(player -> {
                if (player != null) {
                    viewers.add(player);
                }
            });
            return viewers.build();
        } finally {
            viewerLock.readLock().unlock();
            try { // Clean up the viewers
                viewerLock.writeLock().lock();
                this.viewers = this.viewers.stream()
                        .filter(weakPlayer -> weakPlayer.get() != null)
                        .collect(Collectors.toList());
            } finally {
                viewerLock.writeLock().unlock();
            }
        }
    }

    /** Generate the packets to send and destroy the hologram */
    private void generate() {
        int[] ids = new int[buffer.size()];
        AtomicInteger counter = new AtomicInteger();
        double y = (buffer.size() / 2) * -OFFSET; // Shift origin so hologram's origin is in the center
        for (Text line : buffer) {
            line(y += OFFSET, line).ifPresent(entity -> {
                Packet packet = new Packet(PLAY_CLIENT_SPAWN_MOB).inject(clazz -> {
                    try { // Swap out the default packet with the one of the entity
                        Class<?> entityClass = Reflections.clazz("net.minecraft.entity.EntityLivingBase").get();
                        return clazz.getConstructor(entityClass).newInstance(entity);
                    } catch (Exception error) {
                        throw Throwables.propagate(error);
                    }
                });
                ids[counter.getAndIncrement()] = entity.hashCode();
                spawnPackets.add(packet);
                armorStands.add(entity);
            });
        }
        // Destroy packet
        destroyPacket = new Packet(PLAY_CLIENT_DESTROY_ENTITIES).injector().add(ids).inject();
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

    @Override
    public int compareTo(Hologram other) {
        return origin.compareTo(other.origin);
    }

    @Override
    public int hashCode() {
        return origin.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other != null && Hologram.class.cast(other).origin.equals(origin);
    }

    @Override
    public String toString() {
        return "Hologram " + origin.toString();
    }
}
