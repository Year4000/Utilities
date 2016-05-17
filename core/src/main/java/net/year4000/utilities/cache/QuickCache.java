/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.cache;

import com.google.common.cache.LoadingCache;

@SuppressWarnings("unused")
public final class QuickCache<T> {
    private Class<T> clazz;
    private LoadingCache<Class<T>, T> cache;

    /** Internal use to create this quick cache */
    QuickCache(Class<T> clazz, LoadingCache<Class<T>, T> cache) {
        this.clazz = clazz;
        this.cache = cache;
    }

    public static <B> QuickCacheBuilder<B> builder(Class<B> clazz) {
        return new QuickCacheBuilder<>(clazz);
    }

    /**
     * Get the instance of T
     *
     * @return The instance of T
     */
    public T get() {
        return cache.getUnchecked(clazz);
    }
}
