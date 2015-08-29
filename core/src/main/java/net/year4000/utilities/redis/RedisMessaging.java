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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.io.Closeable;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

@ToString
@EqualsAndHashCode
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
