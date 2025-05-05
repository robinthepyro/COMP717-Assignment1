package atmp2;

import java.util.ArrayList;

/**
 * Stores duration in nanoseconds
 */
public class DurationTracker implements CSVExportable {
    private ArrayList<Long> durationFrames = new ArrayList<>();
    private boolean running = false;
    private long startTime;

    public void startTracking() {
        if (running)
            return;

        startTime = System.nanoTime();
        running = true;
    }

    public void stopTracking() {
        if (!running)
            return;

        long duration = System.nanoTime() - startTime;
        running = false;
        durationFrames.add(duration);
    }

    public void resetTracker() {
        running = false;
        durationFrames.clear();
    }

    @Override
    public String[] getCSVHeaders() {
        String[] headers = new String[2];
        headers[0] = "frame_num";
        headers[1] = "duration";

        return headers;
    }

    @Override
    public String[][] getCSVData() {
        String[][] data = new String[durationFrames.size()][2];

        for (int i = 0; i < durationFrames.size(); i++) {
            data[i][0] = String.valueOf(i);
            data[i][1] = String.valueOf(durationFrames.get(i));
        }

        return data;
    }
}
