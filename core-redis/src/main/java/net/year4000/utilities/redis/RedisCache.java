/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class RedisCache<T> {
    protected JedisPool pool;
    protected SetParams params;

    /** Set this data cache to store binary data */
    public RedisCache(JedisPool pool, SetParams params) {
        this.pool = checkNotNull(pool);
        this.params = checkNotNull(params);
    }

    /** Set the key to the specific data */
    public abstract void set(String key, T data);

    /** Get the date from the key */
    public abstract T get(String key);
}
