/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.value.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class SchedulerManager implements Scheduler {
    final Map<Integer, ThreadedTask> tasks = new ConcurrentSkipListMap<>();
    private final AtomicInteger counter = new AtomicInteger();
    private final ExecutorService executor;

    private SchedulerManager(ExecutorService executor) {
        this.executor = Conditions.nonNull(executor, "executor");
    }

    @Override
    public void endAll() {
        tasks.values().forEach(ThreadedTask::stop);
        executor.shutdown();
    }

    @Override
    public ThreadedTask run(Runnable task) {
        return schedule(dummy -> task.run(), 0, null, false);
    }

    @Override
    public ThreadedTask run(Runnable task, int delay, TimeUnit unit) {
        return schedule(dummy -> task.run(), delay, unit, false);
    }

    @Override
    public ThreadedTask repeat(Runnable task, int delay, TimeUnit unit) {
        return schedule(dummy -> task.run(), delay, unit, true);
    }

    @Override
    public ThreadedTask run(Consumer<ThreadedTask> task) {
        return schedule(task, 0, null, false);
    }

    @Override
    public ThreadedTask run(Consumer<ThreadedTask> task, int delay, TimeUnit unit) {
        return schedule(task, delay, unit, false);
    }

    @Override
    public ThreadedTask repeat(Consumer<ThreadedTask> task, int delay, TimeUnit unit) {
        return schedule(task, delay, unit, true);
    }

    /** Schedule a task to be ran in the future */
    private ThreadedTask schedule(Consumer<ThreadedTask> task, int delay, TimeUnit unit, boolean repeat) {
        Conditions.nonNull(task, "task");
        Conditions.isLarger(delay, -1);

        final int position = counter.getAndIncrement();
        ThreadedTask threadedTask = new ThreadedTask(this, position, task, delay, unit, repeat);
        tasks.put(position, threadedTask);
        executor.execute(threadedTask);
        return threadedTask;
    }

    /** Get the builder for this scheduler */
    public static Builder builder() {
        return new Builder();
    }

    /** The builder that will create scheduler */
    public static class Builder implements net.year4000.utilities.Builder<Scheduler> {
        private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("Utilities Pool Thread #%1$d").build();
        private Value<ExecutorService> serviceValue = Value.empty();

        /** Set the custom executor for this scheduler */
        public Builder executor(ExecutorService executor) {
            serviceValue = Value.of(executor);
            return this;
        }

        /** Build the Scheduler */
        public Scheduler build() {
            return new SchedulerManager(serviceValue.getOrElse(Executors.newCachedThreadPool(THREAD_FACTORY)));
        }
    }
}
