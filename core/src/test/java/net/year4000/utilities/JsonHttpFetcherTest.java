/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import com.google.gson.JsonObject;
import junit.framework.Assert;
import net.year4000.utilities.net.HttpFetcher;
import net.year4000.utilities.net.JsonHttpFetcher;
import org.junit.Test;

import java.util.logging.Logger;

public class JsonHttpFetcherTest {
    private static final JsonHttpFetcher fetcher = JsonHttpFetcher.builder().build();
    private static final String URL = "https://api.year4000.net";
    private static final String OFFLINE = "502 Bad Gateway";
    private static final Logger log = Logger.getLogger(JsonHttpFetcherTest.class.getName());

    private void test(HttpFetcher.Methods method) {
        try {
            String code = String.valueOf(System.currentTimeMillis());
            String url = URLBuilder.builder(URL).addPath("test").addQuery("code", code).toString();
            JsonObject response;

            switch (method) {
                case GET:
                    response = fetcher.get(url, JsonObject.class);
                    break;
                case POST:
                    response = fetcher.post(url, null, JsonObject.class);
                    break;
                case PUT:
                    response = fetcher.put(url, null, JsonObject.class);
                    break;
                case DELETE:
                    response = fetcher.delete(url, null, JsonObject.class);
                    break;
                default:
                    throw new EnumConstantNotPresentException(HttpFetcher.Methods.class, method.toString());
            }

            Assert.assertEquals(code, response.get("code").getAsString());
        }
        catch (Exception e) {
            if (!e.getMessage().equals(OFFLINE)) {
                log.info("The API is up response message: " + e.getMessage());
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void getTest() {
        test(HttpFetcher.Methods.GET);
    }

    @Test
    public void postTest() {
        test(HttpFetcher.Methods.POST);
    }

    @Test
    public void putTest() {
        test(HttpFetcher.Methods.PUT);
    }

    @Test
    public void deleteTest() {
        test(HttpFetcher.Methods.DELETE);
    }
}
