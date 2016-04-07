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

import lombok.experimental.NonFinal;
import net.year4000.utilities.ObjectHelper;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ThreadedTask implements Runnable {
    private SchedulerManager manager;
    private int id;
    private Runnable task;
    private int delay;
    private TimeUnit unit;
    @NonFinal
    private boolean repeat;

    ThreadedTask(SchedulerManager manager, int id, Runnable task, int delay, TimeUnit unit, boolean repeat) {
        this.manager = Objects.requireNonNull(manager);
        this.id = id;
        this.task = Objects.requireNonNull(task);
        this.delay = delay;
        this.unit = Objects.requireNonNull(unit);
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
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /** Execute the code */
    private void execute() {
        try {
            task.run();
        }
        catch (Exception t) {
            manager.log.log(t, false);
        }
    }

    @Override
    public void run() {
        do {
            if (repeat) {
                execute();
                sleep();
            }
            else {
                sleep();
                execute();
            }
        } while (repeat);
    }

    @Override
    public String toString() {
        return ObjectHelper.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return ObjectHelper.equals(this, other);
    }

    @Override
    public int hashCode() {
        return ObjectHelper.hashCode(this);
    }
}
