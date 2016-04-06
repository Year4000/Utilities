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

import java.util.concurrent.TimeUnit;

public class ThreadedTask implements Runnable {
    private SchedulerManager manager;
    private int id;
    private Runnable task;
    private int delay;
    private TimeUnit unit;
    @NonFinal
    private boolean repeat;

    @java.beans.ConstructorProperties({"manager", "id", "task", "delay", "unit", "repeat"})
    public ThreadedTask(SchedulerManager manager, int id, Runnable task, int delay, TimeUnit unit, boolean repeat) {
        this.manager = manager;
        this.id = id;
        this.task = task;
        this.delay = delay;
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

    public SchedulerManager getManager() {
        return this.manager;
    }

    public int getId() {
        return this.id;
    }

    public Runnable getTask() {
        return this.task;
    }

    public int getDelay() {
        return this.delay;
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public boolean isRepeat() {
        return this.repeat;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ThreadedTask)) return false;
        final ThreadedTask other = (ThreadedTask) o;
        final Object this$manager = this.manager;
        final Object other$manager = other.manager;
        if (this$manager == null ? other$manager != null : !this$manager.equals(other$manager)) return false;
        if (this.id != other.id) return false;
        final Object this$task = this.task;
        final Object other$task = other.task;
        if (this$task == null ? other$task != null : !this$task.equals(other$task)) return false;
        if (this.delay != other.delay) return false;
        final Object this$unit = this.unit;
        final Object other$unit = other.unit;
        if (this$unit == null ? other$unit != null : !this$unit.equals(other$unit)) return false;
        if (this.repeat != other.repeat) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $manager = this.manager;
        result = result * PRIME + ($manager == null ? 0 : $manager.hashCode());
        result = result * PRIME + this.id;
        final Object $task = this.task;
        result = result * PRIME + ($task == null ? 0 : $task.hashCode());
        result = result * PRIME + this.delay;
        final Object $unit = this.unit;
        result = result * PRIME + ($unit == null ? 0 : $unit.hashCode());
        result = result * PRIME + (this.repeat ? 79 : 97);
        return result;
    }

    public String toString() {
        return "net.year4000.utilities.scheduler.ThreadedTask(manager=" + this.manager + ", id=" + this.id + ", task=" + this.task + ", delay=" + this.delay + ", unit=" + this.unit + ", repeat=" + this.repeat + ")";
    }
}
