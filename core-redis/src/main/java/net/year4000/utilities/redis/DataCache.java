/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

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
        super(pool, createSetParams(mode, unit, time));
    }

    /** Set this data cache to store binary data */
    public DataCache(JedisPool pool, byte[] mode, byte[] unit, int time) {
        this(pool, mode, unit, (long) time);
    }

    /** Maintain reverser compatibility for the the lib */
    private static SetParams createSetParams(byte[] mode, byte[] unit, long time) {
        checkArgument(mode == NX || mode == XX, "invalid mode");
        checkArgument(unit == EX || unit == PX, "invalid unit");
        SetParams setParams = SetParams.setParams();
        if (mode == NX) {
            setParams.nx();
        }
        if (mode == XX) {
            setParams.xx();
        }
        if (unit == EX) {
            setParams.ex((int) time);
        }
        if (unit == PX) {
            setParams.px(time);
        }
        return setParams;
    }

    /** Set the key to the specific data */
    @Override
    public void set(String key, byte[] data) {
        try (Jedis jedis = pool.getResource()) {
            byte[] id = key.getBytes();
            jedis.set(id, data, params);
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
