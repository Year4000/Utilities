package net.year4000.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.java.Log;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

@Log
public class PingerTest {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final LogUtil logutil = new LogUtil(log, Boolean.parseBoolean(System.getProperty("test.debug")));

    @Test
    public void ping() throws IOException {
        try {
            // Year4000 Network
            InetSocketAddress year4000 = new InetSocketAddress("mc.year4000.net", 25565);

            Pinger ping = new Pinger(year4000, Pinger.TIME_OUT);

            logutil.debug(gson.toJson(ping.fetchData(), Pinger.StatusResponse.class));
        }
        catch (IOException e) {
            if (e.getMessage().contains("connect timed out") || e.getMessage().contains("Connection refused")) {
                logutil.log("Could not contact Year4000, skipping test.");
            }
            else {
                throw new IOException(e.getMessage());
            }
        }
    }
}
