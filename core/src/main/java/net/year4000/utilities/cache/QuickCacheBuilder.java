/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.year4000.utilities.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;

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
    @NonNull
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
