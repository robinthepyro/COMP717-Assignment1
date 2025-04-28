package atmp2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MemoryTracker {
    private static final Runtime runtime = Runtime.getRuntime();
    private LinkedList<Long> memoryFrames = new LinkedList<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread trackerThread;

    public void startTracking() {
        if (running.get()) return; // prevent the tracker instance being started multiple times.

        running.set(true);
        trackerThread = new Thread(() -> {
            while (running.get()) {
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                memoryFrames.add(usedMemory);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        trackerThread.setDaemon(true);
        trackerThread.start();
    }

    public void stopTracking() {
        running.set(false);
        if (trackerThread != null) {
            try {
                trackerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public LinkedList<Long> getMemoryFrames() {
        return memoryFrames;
    }

    public void printStats() {
        if (memoryFrames.isEmpty()) {
            System.out.println("No memory samples recorded.");
            return;
        }

        long peak = Collections.max(memoryFrames);
        double average = memoryFrames.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        System.out.println("======");
        System.out.println("Peak memory usage: " + (peak / (1024 * 1024)) + " MB");
        System.out.println("Average memory usage: " + (average / (1024 * 1024)) + " MB");
        System.out.println("Samples collected: " + memoryFrames.size());
        System.out.println("======");
    }
}
