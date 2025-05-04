package atmp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class MemoryTracker implements CSVExportable {
    private static final Runtime runtime = Runtime.getRuntime();
    private ArrayList<Long> memoryFrames = new ArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread trackerThread;
    private int resolution = 1;  // in ms, smaller is higher resolution

    public void startTracking() {
        if (running.get()) return; // prevent the tracker instance being started multiple times.

        running.set(true);
        trackerThread = new Thread(() -> {
            while (running.get()) {
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                memoryFrames.add(usedMemory);

                try {
                    Thread.sleep(resolution);
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

    public void resetTracker() {
        stopTracking();
        memoryFrames.clear();
    }

    public ArrayList<Long> getMemoryFrames() {
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

    @Override
    public String[] getCSVHeaders() {
        String[] headers = new String[2];
        headers[0] = "frame_num";
        headers[1] = "memory_bytes";
        return headers;
    }

    @Override
    public String[][] getCSVData() {
        String[][] data = new String[memoryFrames.size()][2];

        for (int i = 0; i < memoryFrames.size(); i++) {
            data[i][0] = String.valueOf(i);
            data[i][1] = String.valueOf(memoryFrames.get(i));
        }

        return data;
    }
}
