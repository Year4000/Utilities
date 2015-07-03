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

package net.year4000.utilities.redis;

import redis.clients.jedis.JedisPool;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class RedisCache<T> {
    protected JedisPool pool;
    protected T mode; // NX|XX
    protected T unit; // PX|EX
    protected long time;

    /** Set this data cache to store binary data */
    public RedisCache(JedisPool pool, T mode, T unit, long time) {
        this.pool = checkNotNull(pool);
        this.mode = checkNotNull(mode);
        this.unit = checkNotNull(unit);
        checkArgument(time > 0, "time must me > 0");
        this.time = time;
    }

    /** Set this data cache to store binary data */
    public RedisCache(JedisPool pool, T mode, T unit, int time) {
        this(pool, mode, unit, (long) time);
    }

    /** Set the key to the specific data */
    public abstract void set(String key, T data);

    /** Get the date from the key */
    public abstract T get(String key);
}
