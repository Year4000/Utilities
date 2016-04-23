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

import com.google.common.base.Throwables;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;

import java.util.concurrent.TimeUnit;

public class ThreadedTask implements Runnable {
    private final SchedulerManager manager;
    private final int id;
    private final Runnable task;
    private final int delay;
    private final TimeUnit unit;
    private boolean repeat;

    ThreadedTask(SchedulerManager manager, int id, Runnable task, int delay, TimeUnit unit, boolean repeat) {
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
            task.run();
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
