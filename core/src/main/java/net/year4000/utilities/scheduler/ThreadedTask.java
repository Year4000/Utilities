package net.year4000.utilities.scheduler;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.concurrent.TimeUnit;

@Value
public class ThreadedTask implements Runnable {
    private SchedulerManager manager;
    private int id;
    private Runnable task;
    private int delay;
    private TimeUnit unit;
    @NonFinal
    private boolean repeat;


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
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /** Execute the code */
    private void execute() {
        try {
            task.run();
        } catch (Exception t) {
            manager.log.log(t, false);
        }
    }

    @Override
    public void run() {
        do {
            execute();
            sleep();
        } while (repeat);
    }
}
