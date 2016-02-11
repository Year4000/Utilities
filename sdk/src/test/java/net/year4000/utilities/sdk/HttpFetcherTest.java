package net.year4000.utilities.sdk;

import com.google.gson.JsonObject;
import junit.framework.Assert;
import lombok.extern.java.Log;
import net.year4000.utilities.URLBuilder;
import org.junit.Test;

@Log
public class HttpFetcherTest {
    private static final String URL = "https://api.year4000.net";
    private static final String OFFLINE = "502 Bad Gateway";
    private enum Methods {GET, POST, PUT, DELETE}

    private void test(Methods method) {
        try {
            String code = String.valueOf(System.currentTimeMillis());
            String url = URLBuilder.builder(URL).addPath("test").addQuery("code", code).toString();
            JsonObject response;

            switch (method) {
                case GET:
                    response = HttpFetcher.get(url, JsonObject.class);
                    break;
                case POST:
                    response = HttpFetcher.post(url, null, JsonObject.class);
                    break;
                case PUT:
                    response = HttpFetcher.put(url, null, JsonObject.class);
                    break;
                case DELETE:
                    response = HttpFetcher.delete(url, null, JsonObject.class);
                    break;
                default:
                    throw new EnumConstantNotPresentException(Methods.class, method.toString());
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
