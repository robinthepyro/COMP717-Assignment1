package atmp2;

public class WinTracker implements CSVExportable {
    private int playerWin = 0;
    private int aiWin = 0;
    private String playerName;
    private String aiName;

    public WinTracker(String playerName, String aiName) {
        this.playerName = playerName;
        this.aiName = aiName;
    }

    public WinTracker() {
        playerName = "player_wins";
        aiName = "ai_wins";
    }

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
        headers[0] = playerName;
        headers[1] = aiName;

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
