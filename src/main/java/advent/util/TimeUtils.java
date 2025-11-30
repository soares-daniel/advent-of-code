package advent.util;

public final class TimeUtils {
    private TimeUtils() {}

    public static void time(String label, Runnable task) {
        long start = System.nanoTime();
        task.run();
        long ms = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("[TIMER] %-15s : %d ms%n", label, ms);
    }
}