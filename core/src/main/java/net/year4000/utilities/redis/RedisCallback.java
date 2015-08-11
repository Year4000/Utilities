package net.year4000.utilities.redis;

import net.year4000.utilities.scheduler.SchedulerManager;
import redis.clients.jedis.JedisPool;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Preconditions.checkNotNull;

public class RedisCallback implements Closeable {
    private final SchedulerManager scheduler = new SchedulerManager();
    private RedisMessaging messaging;
    private long timeout;
    private String response = null;

    public RedisCallback(JedisPool pool, String listen, long timeout, TimeUnit timeUnit) {
        checkNotNull(pool, "pool");
        checkNotNull(listen, "listen");
        checkNotNull(timeUnit, "timeUnit");

        this.timeout = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        messaging = new RedisMessaging(pool);
        messaging.subscribe(listen, this::setResponse);
        scheduler.run(messaging::init);
    }

    public RedisCallback(JedisPool pool, String listen) {
        this(pool, listen, 5, TimeUnit.SECONDS);
    }

    /** Send and wait for the response */
    public String send(String channel, String data) throws TimeoutException {
        checkNotNull(channel, "channel");
        checkNotNull(data, "data");

        response = null;
        messaging.publish(channel, data);

        while (System.currentTimeMillis() <= timeout) {
            if (response != null) {
                return response;
            }

            try {
                Thread.sleep(64);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        throw new TimeoutException("Timeout on response");
    }

    /** This is a hack to avoid finals in lambda functions */
    private void setResponse(String data) {
        if (response == null) {
            response = data;
        }
    }

    @Override
    public void close() {
        messaging.close();
        scheduler.endAll();
    }
}
