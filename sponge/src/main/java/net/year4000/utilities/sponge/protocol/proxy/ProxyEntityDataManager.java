package net.year4000.utilities.sponge.protocol.proxy;

import com.google.common.collect.ImmutableList;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Proxied;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

@Proxied("net.minecraft.network.datasync.EntityDataManager")
public interface ProxyEntityDataManager {
    /** Get the object that this proxy is using */
    Object $this();

    /** The lock used for the data map */
    @Getter(signature = "Ljava/util/concurrent/locks/ReadWriteLock;")
    ReadWriteLock lock();

    /** The watched object map, use with caution use the lock */
    @Getter(signature = "Ljava/util/Map;", index = 1)
    Map<Integer, Object> watchedObjects();

    /** Get the immutable list of all the watchers */
    default List<Object> watching() {
        try {
            lock().readLock().lock();
            return ImmutableList.copyOf(watchedObjects().values());
        } finally {
            lock().readLock().unlock();
        }
    }
}
