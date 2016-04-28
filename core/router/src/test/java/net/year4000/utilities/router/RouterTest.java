package net.year4000.utilities.router;

import org.junit.Assert;
import org.junit.Test;

public class RouterTest {
    private final Router router = Router.builder()
            .path("test", "GET", String.class, (request, response, args) -> "test-get")
            .path("test", "POST", String.class, (request, response, args) -> "test-post")
            .path("test", "PUT", String.class, (request, response, args) -> "test-put")
            .path("dummy", "GET", String.class, (request, response, args) -> "dummy")
            .build();

    @Test
    public void routerBuilderTest() {
        System.out.println(router);
        Assert.assertTrue(router.findPath("dummy", "GET", String.class).isPresent());
        Assert.assertTrue(router.findPath("test", "GET", String.class).isPresent());
        Assert.assertFalse(router.findPath("text", "GET", String.class).isPresent());
    }

    @Test
    public void routedPathTest() {
        RoutedPath<String> path = router.findPath("test", "GET", String.class).get();
        System.out.println(path);
        Assert.assertEquals("test", path.getPrefix());
        Assert.assertEquals("GET", path.getMethod());
        Assert.assertEquals(String.class, path.getContentType());
        Assert.assertEquals("test-get", path._handle(null, null));
        Assert.assertEquals("test-post", router.findPath("test", "POST", String.class).get()._handle(null, null));
        Assert.assertEquals("test-put", router.findPath("test", "PUT", String.class).get()._handle(null, null));
    }
}
