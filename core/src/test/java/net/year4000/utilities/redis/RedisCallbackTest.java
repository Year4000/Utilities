package net.year4000.utilities.redis;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

public class RedisCallbackTest {
    private final String listen = "year4000.utilities.callback";
    private final String string = "This is a callback response string!";
    private JedisPool pool;

    @Before
    public void setup() {
        pool = new JedisPool();
    }

    @Test
    public void test() throws Exception {
        try (RedisCallback callback = new RedisCallback(pool, listen)) {
            String response = callback.send(listen, string);
            Assert.assertEquals(string, response);
        }
    }
}
