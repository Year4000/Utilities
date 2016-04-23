/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
        return schedule(task, 0, null, false);
    }

    @Override
    public ThreadedTask run(Runnable task, int delay, TimeUnit unit) {
        return schedule(task, delay, unit, false);
    }

    @Override
    public ThreadedTask repeat(Runnable task, int delay, TimeUnit unit) {
        return schedule(task, delay, unit, true);
    }

    /** Schedule a task to be ran in the future */
    private ThreadedTask schedule(Runnable task, int delay, TimeUnit unit, boolean repeat) {
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
