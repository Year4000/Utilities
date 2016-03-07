package net.year4000.utilities.sdk;

import com.google.gson.JsonObject;
import junit.framework.Assert;
import lombok.extern.java.Log;
import net.year4000.utilities.URLBuilder;
import net.year4000.utilities.net.HttpFetcher;
import org.junit.Ignore;
import org.junit.Test;

@Log
public class HttpFetcherTest {
    private static final HttpFetcher fetcher = HttpFetcher.builder().build();
    private static final String URL = "https://api.year4000.net";
    private static final String OFFLINE = "502 Bad Gateway";

    private void test(Methods method) {
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
    @Ignore
    public void getTest() {
        test(Methods.GET);
    }

    @Test
    @Ignore
    public void postTest() {
        test(Methods.POST);
    }

    @Test
    @Ignore
    public void putTest() {
        test(Methods.PUT);
    }

    @Test
    @Ignore
    public void deleteTest() {
        test(Methods.DELETE);
    }

    private enum Methods {GET, POST, PUT, DELETE}
}
