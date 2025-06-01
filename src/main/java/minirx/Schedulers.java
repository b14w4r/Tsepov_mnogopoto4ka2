package minirx;

import java.util.concurrent.*;

public class Schedulers {
    public static Scheduler io() {
        return task -> Executors.newCachedThreadPool().submit(task);
    }

    public static Scheduler computation() {
        return task -> Executors.newFixedThreadPool(4).submit(task);
    }

    public static Scheduler single() {
        return task -> Executors.newSingleThreadExecutor().submit(task);
    }
}
