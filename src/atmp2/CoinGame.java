package atmp2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class CoinGame implements Game {
    private final Scanner scanner = new Scanner(System.in);
    private Minimax<CoinGameMove, CoinGameState> aiMinMax;
    private int aiMode;
    private int gameSize;
    private WinTracker winTracker = new WinTracker();
    private CSVWriter<WinTracker> winTrackerCSVWriter = new CSVWriter<>("coingame wins");

    // Demo mode stuff
    private boolean demoMode; // True when AI vs AI
    private int demoPlayCount;
    private int demoGameSize;
    private int demoMinmaxDepth;
    private int playerAiMode;
    private Minimax<CoinGameMove, CoinGameState> playerAiMinMax;
    private MemoryTracker aiMemoryTracker;
    private CSVWriter<MemoryTracker> aiMemoryTrackerCSVWriter;
    private DurationTracker aiDurationTracker;
    private CSVWriter<DurationTracker> aiDurationTrackerCSVWriter;

    private MemoryTracker playerAiMemoryTracker;
    private CSVWriter<MemoryTracker> playerAiMemoryTrackerCSVWriter;
    private DurationTracker playerAiDurationTracker;
    private CSVWriter<DurationTracker> playerAiDurationTrackerCSVWriter;

    public static void main(String[] args) {
        CoinGame game = new CoinGame();
        game.demo();
    }

    public CoinGame(int mode) {
        this.aiMode = mode;
        this.gameSize = 10;
    }

    // Constructor for demo mode
    public CoinGame() {
        this.demoGameSize = 100;
        this.demoPlayCount = 500;
        this.aiMode = Minimax.AB_LIMITED;
        this.playerAiMode = Minimax.AB_LIMITED;
        this.demoMinmaxDepth = 50;

        LocalDateTime timestamp = LocalDateTime.now();
        this.aiMemoryTracker = new MemoryTracker();
        this.aiMemoryTrackerCSVWriter = new CSVWriter<>("coingame ai memory", timestamp);
        this.aiDurationTracker = new DurationTracker();
        this.aiDurationTrackerCSVWriter = new CSVWriter<DurationTracker>("coingame ai duration", timestamp);

        this.playerAiMemoryTracker = new MemoryTracker();
        this.playerAiMemoryTrackerCSVWriter = new CSVWriter<>("coingame playerai memory", timestamp);
        this.playerAiDurationTracker = new DurationTracker();
        this.playerAiDurationTrackerCSVWriter = new CSVWriter<DurationTracker>("coingame playerai duration", timestamp);

    }

    @Override
    public void run() {
        System.out.println("Welcome to the Coin Game");
        CoinGameState state = initializeGame();
        playGame(state);
    }

    @Override
    public void demo() {
        demoMode = true;

        // init game etc
        CoinGameState state = new CoinGameState(CoinGameState.PLAYER_HUMAN, demoGameSize);
        aiMinMax = new Minimax<>(demoMinmaxDepth, aiMode);
        playerAiMinMax = new Minimax<>(demoMinmaxDepth, playerAiMode);
        playGame(state);

        // Write out logs to file
        try {
            aiMemoryTrackerCSVWriter.writeRun(aiMemoryTracker);
            aiDurationTrackerCSVWriter.writeRun(aiDurationTracker);
            playerAiMemoryTrackerCSVWriter.writeRun(playerAiMemoryTracker);
            playerAiDurationTrackerCSVWriter.writeRun(playerAiDurationTracker);
        } catch (IOException e) {
            e.printStackTrace();
        }

        demoPlayCount--;
        if (demoPlayCount > 0) { // run next test
            aiMemoryTracker.resetTracker();
            aiDurationTracker.resetTracker();
            playerAiMemoryTracker.resetTracker();
            playerAiDurationTracker.resetTracker();

            demo();
        } else {
            try {
                winTrackerCSVWriter.writeRun(winTracker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private CoinGameState initializeGame() {
        boolean playerStarts = getPlayerStartChoice();
        int chosenStartPlayer = playerStarts ? CoinGameState.PLAYER_HUMAN :
                CoinGameState.PLAYER_AI;

        CoinGameState state = new CoinGameState(chosenStartPlayer, gameSize);

        int minmaxDepth = getMinMaxDepth();
        aiMinMax = new Minimax<>(minmaxDepth, aiMode);

        return state;
    }

    /*
     * @return True if the player wants to go first
     */
    private boolean getPlayerStartChoice() {
        System.out.print("Do you want to go first? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.startsWith("y");
    }

    private int getMinMaxDepth() {
        while (true) {
            try {
                System.out.print("Enter Minimax search depth: ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private void playGame(CoinGameState state) {
        while (true) {
            if (state.isTerminal()) break;
            System.out.println("----------");
            clearScreen();
            System.out.println(state);

            if (state.getCurrentPlayer() == CoinGameState.PLAYER_HUMAN) {
                if (demoMode) {
                    handlePlayerAiMove(state);
                } else {
                    state.applyMove(getPlayerMove(state));
                }

            } else {
                handleAIMove(state);
            }

            System.out.println("----------");
            System.out.println("You: " + state.getPlayerScore() + " AI:" + state.getAiScore());

        }

        boolean playerWins = state.getWinner() == CoinGameState.PLAYER_HUMAN;

        if (playerWins) {
            System.out.println("Congratulations! You won!");
            winTracker.playerWins();

        } else {
            System.out.println("You lost...");
            winTracker.aiWins();
        }
        System.out.println("You: " + state.getPlayerScore() + " AI:" + state.getAiScore());
    }

    private CoinGameMove getPlayerMove(CoinGameState state) {
        while (true) {
            System.out.println("Will you pick from the left or right? (l/r)");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.startsWith("l")) {
                return new CoinGameMove(Side.LEFT, state.getCurrentPlayer());
            } else if (input.startsWith("r")) {
                return new CoinGameMove(Side.RIGHT, state.getCurrentPlayer());
            } else {
                System.out.println("Invalid input. Please try again");
            }
        }
    }

    private void handleAIMove(CoinGameState state) {
        System.out.println("AI is thinking...");
        if (aiMemoryTracker != null) aiMemoryTracker.startTracking();
        if (aiDurationTracker != null) aiDurationTracker.startTracking();
        CoinGameMove bestMove = aiMinMax.getBestMove(state, true);
        if (aiDurationTracker != null) aiDurationTracker.stopTracking();
        if (aiMemoryTracker != null) aiMemoryTracker.stopTracking();
        System.out.println("AI plays: " + bestMove);
        state.applyMove(bestMove);
    }

    private void handlePlayerAiMove(CoinGameState state) {
        System.out.println("Player AI is thinking...");
        if (playerAiMemoryTracker != null) playerAiMemoryTracker.startTracking();
        if (playerAiDurationTracker != null) playerAiDurationTracker.startTracking();
        CoinGameMove bestMove = playerAiMinMax.getBestMove(state, false);
        if (playerAiDurationTracker != null) playerAiDurationTracker.stopTracking();
        if (playerAiMemoryTracker != null) playerAiMemoryTracker.stopTracking();
        System.out.println("AI plays: " + bestMove);
        state.applyMove(bestMove);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
