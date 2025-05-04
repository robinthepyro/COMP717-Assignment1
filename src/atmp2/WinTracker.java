package atmp2;

public class WinTracker implements CSVExportable {
    private int playerWin = 0;
    private int aiWin = 0;

    public void playerWins() {
        playerWin++;
    }

    public void aiWins() {
        aiWin++;
    }

    public void resetTracker() {
        playerWin = 0;
        aiWin = 0;
    }

    @Override
    public String[] getCSVHeaders() {
        String[] headers = new String[2];
        headers[0] = "player_wins";
        headers[1] = "ai_wins";

        return headers;
    }

    @Override
    public String[][] getCSVData() {
        String[][] data = new String[1][2];
        data[0][0] = String.valueOf(playerWin);
        data[0][1] = String.valueOf(aiWin);

        return data;
    }
}
