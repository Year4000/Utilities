/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.redis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.year4000.utilities.Conditions;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.io.Closeable;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RedisMessaging implements Closeable {
    private static final String CHANNELS = "*";
    private final Multimap<String, Consumer<String>> listeners = ArrayListMultimap.create();
    private transient boolean init = false;
    private JedisPool jedisPool;
    private PubSub pubsub = new PubSub();

    /** Construct this instance with null checks */
    public RedisMessaging(JedisPool pool) {
        jedisPool = checkNotNull(pool, "pool");
    }

    /** Init the Redis messaging in current thread and return self */
    public RedisMessaging init() {
        checkArgument(!init, "init");
        init = true;

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.psubscribe(pubsub, CHANNELS);
        }

        return this;
    }

    /** Unregister all listeners from the Redis channel */
    public void unsubscribe(String channel) {
        checkNotNull(channel);
        checkInit();

        synchronized (listeners) {
            listeners.removeAll(channel);
        }
    }

    /** Register a listener on the Redis channel */
    public void subscribe(String channel, Consumer<String> data) {
        checkNotNull(channel);
        checkNotNull(data);
        checkInit();

        synchronized (listeners) {
            listeners.put(channel, data);
        }
    }

    /** Publish a message on the channel */
    public void publish(String channel, String message) {
        checkNotNull(channel);
        checkNotNull(message);

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        }
    }

    /** Let the developer know the init has not been ran yet */
    private void checkInit() {
        if (!init) {
            System.err.println("Run RedisMessaging::init() to enable RedisMessaging::subscribe()");
        }
    }

    /** Close this RedisMessaging listener thread */
    @Override
    public void close() {
        listeners.keySet().forEach(this::unsubscribe);
        pubsub.unsubscribe();
    }

    @Override
    public String toString() {
        return Conditions.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Conditions.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Conditions.hashCode(this);
    }

    /** Handle the message from Redis */
    public class PubSub extends JedisPubSub {
        @Override
        public void onPMessage(String pattern, String channel, String message) {
            if (listeners.containsKey(channel)) {
                listeners.get(channel).forEach(consumer -> consumer.accept(message));
            }
        }
    }
}
