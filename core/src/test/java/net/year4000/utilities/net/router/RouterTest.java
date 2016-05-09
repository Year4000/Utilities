package net.year4000.utilities.net.router;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RouterTest {
    private static Router router;
    private final static Router.Builder routerBuilder = Router.builder()
            .path("test", "GET", String.class, (request, response, args) -> "test-get")
            .path("test", "POST", String.class, (request, response, args) -> "test-post")
            .path("test", "PUT", String.class, (request, response, args) -> "test-put")
            .path("dummy", "GET", String.class, (request, response, args) -> "dummy");

    @BeforeClass
    public static void setup() {
        for (char letter : "qwertyuiopasdfghjklzxcvbnm".toCharArray()) {
            String alphabet = letter + "" + letter;
            routerBuilder.path(alphabet + "", "GET", String.class, (request, response, args) -> alphabet);
        }
        router = routerBuilder.build();
    }

    @Test
    public void routerBuilderTest() {
        System.out.println(router);
        ((RoutingManager) router).keys().forEach(System.out::println);
        Assert.assertTrue(router.findPath("dummy", "GET", String.class).isPresent());
        Assert.assertTrue(router.findPath("test", "GET", String.class).isPresent());
        Assert.assertTrue(router.findPath("aa", "GET", String.class).isPresent());
        Assert.assertTrue(router.findPath("jj", "GET", String.class).isPresent());
        Assert.assertTrue(router.findPath("ss", "GET", String.class).isPresent());
        Assert.assertFalse(router.findPath("text", "GET", String.class).isPresent());
    }

    @Test
    public void routedPathTest() {
        RoutedPath<String> path = router.findPath("test", "GET", String.class).get();
        System.out.println(path);
        Assert.assertEquals("test", path.getEndPoint());
        Assert.assertEquals("GET", path.getMethod());
        Assert.assertEquals(String.class, path.getContentType());
        Assert.assertEquals("test-get", path._handle(null, null));
        Assert.assertEquals("test-post", router.findPath("test", "POST", String.class).get()._handle(null, null));
        Assert.assertEquals("test-put", router.findPath("test", "PUT", String.class).get()._handle(null, null));
    }
}
