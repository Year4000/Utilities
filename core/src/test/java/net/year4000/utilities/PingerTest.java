/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.year4000.utilities.net.Pinger;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.logging.Logger;

public class PingerTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger log = Logger.getLogger(PingerTest.class.getName());
    private static final LogUtil logutil = new LogUtil(log, Boolean.parseBoolean(System.getProperty("test.debug")));
    private static final Set<String> OK_ERRORS = Sets.newHashSet(
        "connect timed out",
        "Connection refused"
    );

    @Test
    @Ignore
    public void ping() throws IOException {
        try {
            // Minecraft Server
            InetSocketAddress year4000 = new InetSocketAddress("us.mineplex.com", 25565);

            Pinger ping = new Pinger(year4000, Pinger.TIME_OUT);

            logutil.debug(gson.toJson(ping.fetchData(), Pinger.StatusResponse.class));
        }
        catch (IOException e) {
            if (OK_ERRORS.contains(e.getMessage())) {
                logutil.log("Could not contact server, skipping test.");
            }
            else {
                throw new IOException(e.getMessage());
            }
        }
    }
}
