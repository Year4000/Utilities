package net.year4000.utilities.scheduler;


import java.util.concurrent.TimeUnit;

public interface Scheduler {
    static SchedulerManager.Builder builder() {
        return SchedulerManager.builder();
    }

    /** End all pending tasks */
    void endAll();

    /** Run a task in its own thread */
    ThreadedTask run(Runnable task);

    /** Schedule a task to be ran in the future */
    ThreadedTask run(Runnable task, int delay, TimeUnit unit);

    /** Schedule a task to be repeated */
    ThreadedTask repeat(Runnable task, int delay, TimeUnit unit);
}
