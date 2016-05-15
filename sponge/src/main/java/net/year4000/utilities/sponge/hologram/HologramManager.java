package net.year4000.utilities.sponge.hologram;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.scheduler.Scheduler;
import net.year4000.utilities.scheduler.ThreadedTask;
import net.year4000.utilities.sponge.protocol.Packets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * The Hologram Manager that will handle all of the holograms, it uses a FrameBuffer to
 * create the frames for the hologram, if they are animated.
 */
public class HologramManager implements Holograms {
    private static final Map<Class<?>, HologramManager> managers = Maps.newConcurrentMap();
    final Object plugin;
    final Packets packets;
    final Scheduler scheduler;

    protected HologramManager(Object plugin) {
        this.plugin = Conditions.nonNull(plugin, "plugin");
        this.packets = Packets.manager(plugin);
        this.scheduler = Scheduler.builder().executor(Sponge.getScheduler().createAsyncExecutor(plugin)).build();
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
            ImageReader reader = ImageIO.getImageReaders(image).next();
            reader.setInput(image);
            int frames = reader.getNumImages(true);
            FrameBuffer start = FrameBuffer.builder().add(reader.read(0)).build();
            Hologram hologram = new Hologram(this, location, start);
            final SoftReference<Hologram> softHologram = new SoftReference<>(hologram);
            send(players, hologram);
            // Handle the animations, must happen after first frame was sent
            if (frames > 1) {
                List<FrameBuffer> frameBuffers = Lists.newArrayList();
                frameBuffers.add(start);
                for (int i = 1 ; i < frames; i++) {
                    frameBuffers.add(FrameBuffer.builder().add(reader.read(i)).build());
                }
                Iterator<FrameBuffer> iterator = Iterators.cycle(frameBuffers);
                AtomicReference<ThreadedTask> task = new AtomicReference<>();
                task.set(scheduler.repeat(() -> { // Add a task to update the hologram animation
                    Hologram tmp = softHologram.get();
                    if (tmp == null) {
                        task.get().stop();
                        task.set(null);
                    } else {
                        Collection<Player> viewers = tmp.viewers();
                        if (viewers.size() > 0) {
                            viewers.forEach(player -> tmp.update(player, iterator.next()));
                        } else { // Start to enqueue the hologram reference
                            softHologram.clear();
                        }
                    }
                }, 125, TimeUnit.MILLISECONDS));
            }
            return hologram;
        } catch (IOException exception) {
            throw ErrorReporter.builder(exception)
                .add("Player(s): ", players)
                .add("Location: ", location)
                .buildAndReport(System.err);
        }
    }
}
