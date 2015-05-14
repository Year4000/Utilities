package net.year4000.utilities.sdk;

import com.google.gson.JsonObject;
import junit.framework.Assert;
import lombok.extern.java.Log;
import net.year4000.utilities.URLBuilder;
import net.year4000.utilities.sdk.HttpFetcher;
import org.junit.Test;

@Log
public class HttpFetcherTest {
    private static final String URL = "https://api.year4000.net";
    private static final String OFFLINE = "502 Bad Gateway";
    private enum Methods {GET, POST, PUT, DELETE}

    private void test(Methods method) {
        try {
            String code = String.valueOf(System.currentTimeMillis());
            URLBuilder.builder(URL).addPath("test").addQuery("code", code);
            JsonObject response;

            switch (method) {
                case GET:
                    response = HttpFetcher.get(URL, JsonObject.class);
                    break;
                case POST:
                    response = HttpFetcher.post(URL, null, JsonObject.class);
                    break;
                case PUT:
                    response = HttpFetcher.put(URL, null, JsonObject.class);
                    break;
                case DELETE:
                    response = HttpFetcher.delete(URL, null, JsonObject.class);
                    break;
                default:
                    throw new EnumConstantNotPresentException(Methods.class, method.toString());
            }

            Assert.assertEquals(code, response.get("code").getAsString());
        }
        catch (Exception e) {
            if (!e.getMessage().equals(OFFLINE)) {
                log.info("The API could be down: " + e.getMessage());
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void getTest() {
        test(Methods.GET);
    }

    @Test
    public void postTest() {
        test(Methods.POST);
    }

    @Test
    public void putTest() {
        test(Methods.PUT);
    }

    @Test
    public void deleteTest() {
        test(Methods.DELETE);
    }
}
