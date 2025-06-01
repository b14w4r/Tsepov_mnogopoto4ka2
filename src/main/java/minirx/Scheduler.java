package minirx;

public interface Scheduler {
    void execute(Runnable task);
}
