package atmp2;

public interface CSVExportable {
    String[] getCSVHeaders();

    String[][] getCSVData();
}
