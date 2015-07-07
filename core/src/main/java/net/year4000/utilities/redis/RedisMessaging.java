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

import com.google.common.collect.Maps;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

@ToString
@EqualsAndHashCode
public class RedisMessaging {
    private static final String CHANNELS = "*";
    private final ConcurrentMap<String, Consumer<String>> listeners = Maps.newConcurrentMap();
    private transient boolean init = false;
    private JedisPool jedisPool;

    /** Construct this instance with null checks */
    public RedisMessaging(JedisPool pool) {
        jedisPool = checkNotNull(pool, "pool");
    }

    /** Init the Redis messaging in current thread and return self */
    public RedisMessaging init() {
        checkArgument(!init, "init");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.psubscribe(new PubSub(), CHANNELS);
        }

        return this;
    }

    /** Init the Redis messaging in current thread and return self */
    public Runnable run() {
        checkArgument(!init, "init");

        return () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.psubscribe(new PubSub(), CHANNELS);
            }
        };
    }

    /** Unregister a listener from the Redis channel */
    public void unsubscribe(String channel) {
        checkNotNull(channel);
        listeners.remove(channel);
    }

    /** Register a listener on the Redis channel */
    public void subscribe(String channel, Consumer<String> data) {
        checkNotNull(channel);
        checkNotNull(data);
        listeners.put(channel, data);
    }

    /** Publish a message on the channel */
    public void publish(String channel, String message) {
        checkNotNull(channel);
        checkNotNull(message);

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        }
    }

    /** Handle the message from Redis */
    public class PubSub extends JedisPubSub {
        @Override
        public void onPMessage(String pattern, String channel, String message) {
            if (listeners.containsKey(channel)) {
                listeners.get(channel).accept(message);
            }
        }
    }
}
