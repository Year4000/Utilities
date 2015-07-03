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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static com.google.common.base.Preconditions.checkArgument;

public class DataCache extends RedisCache<byte[]> {
    /** Only set the key if it does not already exist */
    public static final byte[] NX = "NX".getBytes();
    /** Only set the key if it already exist */
    public static final byte[] XX = "XX".getBytes();

    /** Set the specified expire time, in seconds */
    public static final byte[] EX = "EX".getBytes();
    /** Set the specified expire time, in milliseconds */
    public static final byte[] PX = "PX".getBytes();

    /** Set this data cache to store binary data */
    public DataCache(JedisPool pool, byte[] mode, byte[] unit, long time) {
        super(pool, mode, unit, time);
        checkArgument(mode == NX || mode == XX, "invalid mode");
        checkArgument(unit == EX || unit == PX, "invalid unit");
    }

    /** Set this data cache to store binary data */
    public DataCache(JedisPool pool, byte[] mode, byte[] unit, int time) {
        this(pool, mode, unit, (long) time);
    }

    /** Set the key to the specific data */
    @Override
    public void set(String key, byte[] data) {
        try (Jedis jedis = pool.getResource()) {
            byte[] id = key.getBytes();
            jedis.set(id, data, mode, unit, time);
        }
    }

    /** Get the date from the key */
    @Override
    public byte[] get(String key) {
        try (Jedis jedis = pool.getResource()) {
            byte[] id = key.getBytes();
            return jedis.get(id);
        }
    }
}
