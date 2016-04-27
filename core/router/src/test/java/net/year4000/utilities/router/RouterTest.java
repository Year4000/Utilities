package net.year4000.utilities.router;

import org.junit.Assert;
import org.junit.Test;

public class RouterTest {
    @Test
    public void routerBuilderTest() {
        Router router = Router.builder().path("test", "GET", String.class, (request, response, args) -> "test").build();
        Assert.assertTrue(router.findPath("test").isPresent());
        Assert.assertFalse(router.findPath("text").isPresent());
        Assert.assertEquals("test", router.findPath("test").get()._handle(null, null));
    }
}
