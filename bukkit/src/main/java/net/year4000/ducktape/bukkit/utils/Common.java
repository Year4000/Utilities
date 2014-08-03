package net.year4000.ducktape.bukkit.utils;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

public final class Common {
    private Common() {/* Util */}

    /** Convert a time unit to Minecraft ticks */
    public static long toTicks(long period, TimeUnit unit) {
        checkArgument(unit != TimeUnit.MICROSECONDS || unit != TimeUnit.MILLISECONDS || unit != TimeUnit.NANOSECONDS);
        return TimeUnit.SECONDS.convert(period, unit) * 20L;
    }
}
