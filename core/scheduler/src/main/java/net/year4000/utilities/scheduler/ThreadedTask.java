/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.scheduler;

import com.google.common.base.Throwables;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ThreadedTask implements Runnable {
    private final SchedulerManager manager;
    private final int id;
    private final Consumer<ThreadedTask> task;
    private final int delay;
    private final TimeUnit unit;
    private boolean repeat;

    ThreadedTask(SchedulerManager manager, int id, Consumer<ThreadedTask> task, int delay, TimeUnit unit, boolean repeat) {
        this.manager = Conditions.nonNull(manager, "manager");
        this.id = Conditions.isLarger(id, -1);
        this.task = Conditions.nonNull(task, "task");
        this.delay = Conditions.isLarger(delay, -1);
        this.unit = unit;
        this.repeat = repeat;
    }

    /** Stop the task if the task was assigned to be repeated */
    public void stop() {
        repeat = false;
        manager.tasks.remove(id);
    }

    /** Sleep the thread until it needs to run again */
    private void sleep() {
        if (delay > 0 && unit != null) {
            try {
                unit.sleep(delay);
            } catch (InterruptedException error) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /** Execute the code */
    private void execute() {
        try {
            task.accept(this);
        } catch (Exception error) {
            Throwables.propagate(error);
        }
    }

    @Override
    public void run() {
        do {
            if (repeat) {
                execute();
                sleep();
            } else {
                sleep();
                execute();
            }
        } while (repeat);
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }
}
