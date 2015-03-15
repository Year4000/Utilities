package net.year4000.utilities.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.year4000.utilities.LogUtil;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SchedulerManager {
    private final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
        .setNameFormat("Utilities Pool Thread #%1$d")
        .build();
    private final ExecutorService EXECUTOR = Executors.newCachedThreadPool(THREAD_FACTORY);
    private final AtomicInteger counter = new AtomicInteger();
    final LogUtil log;
    final Map<Integer, ThreadedTask> tasks = new ConcurrentSkipListMap<>();

    public SchedulerManager() {
        this(new LogUtil());
    }

    public SchedulerManager(LogUtil log) {
        this.log = log;
    }

    /** End all pending tasks */
    public void endAll() {
        tasks.values().forEach(ThreadedTask::stop);
    }

    /** Run a task in its own thread */
    public ThreadedTask run(Runnable task) {
        return schedule(task, 0, null, false);
    }

    /** Schedule a task to be ran in the future */
    public ThreadedTask run(Runnable task, int delay, TimeUnit unit) {
        return schedule(task, delay, unit, false);
    }

    /** Schedule a task to be repeated */
    public ThreadedTask repeat(Runnable task, int delay, TimeUnit unit) {
        return schedule(task, delay, unit, true);
    }

    /** Schedule a task to be ran in the future */
    private ThreadedTask schedule(Runnable task, int delay, TimeUnit unit, boolean repeat) {
        checkNotNull(task);

        final int position = counter.getAndIncrement();
        ThreadedTask threadedTask = new ThreadedTask(this, position, task, delay, unit, repeat);
        tasks.put(position, threadedTask);
        EXECUTOR.execute(threadedTask);
        return threadedTask;
    }
}
