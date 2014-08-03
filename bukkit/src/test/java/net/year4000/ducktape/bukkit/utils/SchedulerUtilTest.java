package net.year4000.ducktape.bukkit.utils;

import junit.framework.Assert;
import lombok.extern.java.Log;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static net.year4000.ducktape.bukkit.utils.Common.toTicks;

@Log
public class SchedulerUtilTest {
    @Test
    public void check() {
        Assert.assertEquals(toTicks(5, TimeUnit.SECONDS), 5 * 20L);
        Assert.assertEquals(toTicks(5, TimeUnit.MINUTES), 5 * 60 * 20L);
        Assert.assertEquals(toTicks(5, TimeUnit.HOURS), 5 * 60 * 60 * 20L);
    }
}
