package net.year4000.utilities.api;

import com.google.gson.JsonObject;
import junit.framework.Assert;
import lombok.extern.java.Log;
import org.junit.Test;

@Log
public class HttpFetcherTest {
    private static final String URL = "https://api.year4000.net";

    @Test
    public void getTest() {
        try {
            JsonObject response = HttpFetcher.get(URL, JsonObject.class);
            Assert.assertEquals(response.get("name").getAsString(), "Year4000-API");
        }
        catch (Exception e) {
            log.info("The API could be down: " + e.getMessage());
        }
    }

    @Test
    public void postTest() {
        try {
           HttpFetcher.post(URL, null, JsonObject.class);
        }
        catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "405 Method Not Allowed");
        }
    }

    @Test
    public void putTest() {
        try {
            HttpFetcher.put(URL, null, JsonObject.class);
        }
        catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "405 Method Not Allowed");
        }
    }

    @Test
    public void deleteTest() {
        try {
            HttpFetcher.delete(URL, null, JsonObject.class);
        }
        catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "405 Method Not Allowed");
        }
    }
}
