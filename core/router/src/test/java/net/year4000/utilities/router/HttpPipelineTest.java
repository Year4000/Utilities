package net.year4000.utilities.router;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import net.year4000.utilities.router.pipline.HttpInitializer;
import org.junit.Assert;
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
        channel = new EmbeddedChannel();
        channel.pipeline().addFirst(new HttpInitializer(router));
    }

    @Test
    public void test() throws Exception {
        Assert.assertTrue(channel.writeInbound(dummyHttp("http://localhost/test")));
        System.out.println((Object) channel.readOutbound());
    }
}
