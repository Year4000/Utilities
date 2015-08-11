package net.year4000.utilities.redis;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Preconditions.checkNotNull;

public class RedisCallback implements Closeable {
    private RedisMessaging messaging;
    private long timeout;
    private String listening;
    private String response;


    public RedisCallback(RedisMessaging redis, String listen, long timeout, TimeUnit timeUnit) {
        checkNotNull(timeUnit, "timeUnit");
        messaging = checkNotNull(redis, "redis");
        listening = checkNotNull(listen, "listen");

        this.timeout = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        messaging.subscribe(listen, this::setResponse);
    }

    public RedisCallback(RedisMessaging messaging, String listen) {
        this(messaging, listen, 5, TimeUnit.SECONDS);
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

        throw new TimeoutException("Timeout on response channel " + listening);
    }

    /** This is a hack to avoid finals in lambda functions */
    private void setResponse(String data) {
        if (response == null) {
            response = data;
        }
    }

    @Override
    public void close() {
        messaging.unsubscribe(listening);
    }
}
