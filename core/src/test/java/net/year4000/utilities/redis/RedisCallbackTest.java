package net.year4000.utilities.redis;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

public class RedisCallbackTest {
    private final String listen = "year4000.utilities.callback";
    private final String string = "This is a callback response string!";
    private JedisPool pool;
    private RedisMessaging messaging;
    private Thread thread;

    @Before
    public void setup() {
        pool = new JedisPool();
        messaging = new RedisMessaging(pool);
        thread = new Thread(messaging::init);
        thread.start();
    }

    @After
    public void after() {
        thread.interrupt();
        messaging.close();
        pool.close();
    }

    @Test
    public void test() throws Exception {
        try (RedisCallback callback = new RedisCallback(messaging, listen)) {
            Thread.sleep(250);
            String response = callback.send(listen, string);
            Assert.assertEquals(string, response);
        }
    }
}
