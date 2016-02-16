/*
 * Copyright 2016 Year4000. All Rights Reserved.
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
