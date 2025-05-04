package atmp2;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter<T extends CSVExportable> {
    private String datedFileName;

    public CSVWriter(String fileName) {
        datedFileName = makeDatedFileName(fileName);
    }

    public void writeRun(T obj) throws IOException {
        File file = new File(datedFileName);
        boolean fileExists = file.exists();
        int runNumber = 1;

        if (fileExists) {
            String lastLine = getLastNonEmptyLine(file);
            if (lastLine != null) {
                String[] parts = lastLine.split(",", 2);
                try {
                    runNumber = Integer.parseInt(parts[0].trim()) + 1;
                } catch (NumberFormatException e) {
                    // Not a valid number, just leave it as 1
                }
            }
        }

        try (FileWriter writer = new FileWriter(file, true)) {
            if (!fileExists) {
                String[] headers = obj.getCSVHeaders();
                writer.write("run," + String.join(",", headers));
                writer.write("\n");
            }

            String[][] data = obj.getCSVData();
            for (String[] row : data) {
                writer.write(runNumber + "," + String.join(",", row));
                writer.write("\n");
            }
        }
    }

    private String getLastNonEmptyLine(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("run")) {
                    lines.add(line);
                }
            }
        }

        return lines.isEmpty() ? null : lines.get(lines.size() - 1);
    }

    private static String makeDatedFileName(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);

        return "logs/" + timestamp + " " + fileName + ".csv";
    }

    public static void exportToCSV(CSVExportable obj, String fileName) throws IOException {
        String datedFileName = makeDatedFileName(fileName);

        try (FileWriter writer = new FileWriter(datedFileName)) {
            String[] headers = obj.getCSVHeaders();
            writer.write(String.join(",", headers));
            writer.write("\n");

            String[][] data = obj.getCSVData();
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        }
    }
}
