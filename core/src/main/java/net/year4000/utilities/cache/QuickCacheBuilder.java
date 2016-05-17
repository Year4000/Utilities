/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unused")
public final class QuickCacheBuilder<T> {
    private Class<T> clazz;
    private int size = 1;
    private int time = 1;
    private TimeUnit unit = TimeUnit.HOURS;

    QuickCacheBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Set the size of the cache.
     *
     * @param size The new size of the cache.
     * @return this
     */
    public QuickCacheBuilder<T> setSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * Set the time length of the cache.
     *
     * @param time The new time length.
     * @return this
     */
    public QuickCacheBuilder<T> setTime(int time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
        return this;
    }

    /**
     * Create a quick cache instance.
     *
     * @return new QuickCache with this builder's info.
     */
    public QuickCache<T> build() {
        return build(clazz, time, unit);
    }

    /** The internal build method */
    private QuickCache<T> build(Class<T> clazz, int time, TimeUnit unit) {
        LoadingCache<Class<T>, T> builder = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(time, checkNotNull(unit))
            .build(new CacheLoader<Class<T>, T>() {
                @Override
                @SuppressWarnings("NullableProblems")
                public T load(Class<T> notUsed) throws Exception {
                    Constructor<T> constructor = checkNotNull(clazz).getConstructor();
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                }
            });

        return new QuickCache<>(clazz, builder);
    }
}
