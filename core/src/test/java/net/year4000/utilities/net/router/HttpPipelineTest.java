/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import net.year4000.utilities.net.router.pipline.HttpInitializer;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpPipelineTest {
    private static EmbeddedChannel channel;
    private static Router router = Router.builder().path("test", "GET", String.class, ((request, response, args) -> "test")).build();

    /** Create the http request with the following url */
    private HttpRequest dummyHttp(String url) {
        return new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
    }

    @BeforeClass
    public static void setup() {
        channel = new EmbeddedChannel(new HttpInitializer(router));
    }

    @Test
    public void test() throws Exception {
        channel.writeInbound(dummyHttp("http://localhost/test"));
        Object buf = channel.readOutbound();
        System.out.println(buf);
    }
}
