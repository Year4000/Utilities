/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.java.Log;
import net.year4000.utilities.net.Pinger;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;

@Log
public class PingerTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final LogUtil logutil = new LogUtil(log, Boolean.parseBoolean(System.getProperty("test.debug")));
    private static final Set<String> OK_ERRORS = Sets.newHashSet(
        "connect timed out",
        "Connection refused"
    );

    @Test
    @Ignore
    public void ping() throws IOException {
        try {
            // Year4000 Network
            InetSocketAddress year4000 = new InetSocketAddress("mc.year4000.net", 25565);

            Pinger ping = new Pinger(year4000, Pinger.TIME_OUT);

            logutil.debug(gson.toJson(ping.fetchData(), Pinger.StatusResponse.class));
        }
        catch (IOException e) {
            if (OK_ERRORS.contains(e.getMessage())) {
                logutil.log("Could not contact Year4000, skipping test.");
            }
            else {
                throw new IOException(e.getMessage());
            }
        }
    }
}
