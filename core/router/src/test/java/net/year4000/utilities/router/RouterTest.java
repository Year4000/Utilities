package net.year4000.utilities.router;

import org.junit.Assert;
import org.junit.Test;

public class RouterTest {
    @Test
    public void routerBuilderTest() {
        Router router = Router.builder()
                .path("test", "GET", String.class, (request, response, args) -> "test-get")
                .path("test", "POST", String.class, (request, response, args) -> "test-post")
                .path("test", "PUT", String.class, (request, response, args) -> "test-put")
                .path("dummy", "GET", String.class, (request, response, args) -> "dummy")
                .build();
        Assert.assertTrue(router.findPath("test", "GET", String.class).isPresent());
        Assert.assertFalse(router.findPath("text", "GET", String.class).isPresent());
        Assert.assertEquals("test-get", router.findPath("test", "GET", String.class).get()._handle(null, null));
        Assert.assertEquals("test-post", router.findPath("test", "POST", String.class).get()._handle(null, null));
        Assert.assertEquals("test-put", router.findPath("test", "PUT", String.class).get()._handle(null, null));

        System.out.println(router);
    }
}
