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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
     * @return The instance of T
     */
    public T get() {
        return cache.getUnchecked(clazz);
    }
}
