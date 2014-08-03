package net.year4000.ducktape.bukkit.utils;

import net.year4000.ducktape.bukkit.DuckTape;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

import static net.year4000.ducktape.bukkit.utils.Common.toTicks;

@SuppressWarnings("unused")
public final class SchedulerUtil {
    private static DuckTape plugin = DuckTape.get();
    private static BukkitScheduler scheduler = Bukkit.getScheduler();

    private SchedulerUtil() {/* Util */}

    /** Run a task in sync with a delay */
    public static BukkitTask runSync(Runnable task, long delay, TimeUnit unit) {
        return scheduler.runTaskLater(plugin, task, toTicks(delay, unit));
    }

    /** Run a task in sync */
    public static BukkitTask runSync(Runnable task) {
        return scheduler.runTask(plugin, task);
    }

    /** Run a task Async with a delay */
    public static BukkitTask runAsync(Runnable task, long delay, TimeUnit unit) {
        return scheduler.runTaskLaterAsynchronously(plugin, task, toTicks(delay, unit));
    }

    /** Run a task Async */
    public static BukkitTask runAsync(Runnable task) {
        return scheduler.runTaskAsynchronously(plugin, task);
    }

    /** Schedule a repeating task sync */
    public static BukkitTask repeatSync(Runnable task, long delay, TimeUnit unit) {
        return scheduler.runTaskTimer(plugin, task, 0, toTicks(delay, unit));
    }

    /** Schedule a repeating task sync with a delay */
    public static BukkitTask repeatSync(Runnable task, long delay, long period, TimeUnit unit) {
        return scheduler.runTaskTimer(plugin, task, delay, toTicks(period, unit));
    }
    /** Schedule a repeating task async */
    public static BukkitTask repeatAsync(Runnable task, long delay, TimeUnit unit) {
        return scheduler.runTaskTimerAsynchronously(plugin, task, 0, toTicks(delay, unit));
    }

    /** Schedule a repeating task async with a delay */
    public static BukkitTask repeatAsync(Runnable task, long delay, long period, TimeUnit unit) {
        return scheduler.runTaskTimerAsynchronously(plugin, task, delay, toTicks(period, unit));
    }
}
