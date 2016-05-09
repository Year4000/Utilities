/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk;

import net.year4000.utilities.sdk.routes.accounts.AccountRoute;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

public class RouteTest {
    private static final Logger log = Logger.getLogger(RouteTest.class.getName());
    private static API api = new API();

    @Test
    @Ignore
    public void accountTest() {
        AccountRoute response = api.getAccount("54c572bba6946f1b42c0bd0e");
        Assert.assertEquals(response.getUsername(), "Year4000");
        Assert.assertEquals(response.getUUID(), "96e51f12-2c2f-42a6-a2d0-045d1eb4b5b2");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @Ignore
    public void accountAsyncTest() {
        api.getAccountAsync("54c572bba6946f1b42c0bd0e", (response, error) -> {
            if (error.isPresent() || !response.isPresent()) {
                throw new RuntimeException(error.orElse(null));
            }

            Assert.assertEquals(response.get().getUsername(), "Year4000");
            Assert.assertEquals(response.get().getUUID(), "96e51f12-2c2f-42a6-a2d0-045d1eb4b5b2");
        });
    }
}
