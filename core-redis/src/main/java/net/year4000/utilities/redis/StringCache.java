/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import static com.google.common.base.Preconditions.checkArgument;

public class StringCache extends RedisCache<String> {
    /** Only set the key if it does not already exist */
    public static final String NX = "NX";
    /** Only set the key if it already exist */
    public static final String XX = "XX";

    /** Set the specified expire time, in seconds */
    public static final String EX = "EX";
    /** Set the specified expire time, in milliseconds */
    public static final String PX = "PX";

    /** Set this data cache to store binary data */
    public StringCache(JedisPool pool, String mode, String unit, long time) {
        super(pool, createSetParams(mode, unit, time));
    }

    /** Set this data cache to store binary data */
    public StringCache(JedisPool pool, String mode, String unit, int time) {
        this(pool, mode, unit, (long) time);
    }

    /** Maintain reverser compatibility for the the lib */
    private static SetParams createSetParams(String mode, String unit, long time) {
        checkArgument(mode.equals(NX) || mode.equals(XX), "invalid mode");
        checkArgument(unit.equals(EX) || unit.equals(PX), "invalid unit");
        SetParams setParams = SetParams.setParams();
        if (mode.equals(NX)) {
            setParams.nx();
        }
        if (mode.equals(XX)) {
            setParams.xx();
        }
        if (unit.equals(EX)) {
            setParams.ex((int) time);
        }
        if (unit.equals(PX)) {
            setParams.px(time);
        }
        return setParams;
    }

    /** Set the key to the specific string */
    @Override
    public void set(String key, String data) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, data, params);
        }
    }

    /** Get the string from the key */
    @Override
    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }
}
