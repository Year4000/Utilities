/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.redis;

import org.junit.*;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

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

    @Test
    public void test() throws Exception {
        try (RedisCallback callback = new RedisCallback(messaging, listen)) {
            Thread.sleep(250);
            String response = callback.send(listen, string);
            Assert.assertEquals(string, response);
        } catch (JedisConnectionException error) {
            thread.interrupt();
            Assume.assumeTrue(false);
        }
    }

    @After
    public void after() {
        if (!thread.isAlive()) return;

        thread.interrupt();
        messaging.close();
        pool.close();
    }
}
