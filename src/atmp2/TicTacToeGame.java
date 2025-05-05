package atmp2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class TicTacToeGame implements Game {
    private static final int X_PLAYER = 1;
    private static final int O_PLAYER = 2;

    private TicTacToeGameState state;
    private Minimax<TicTacToeMove, TicTacToeGameState> minimax;
    private static Scanner scanner = new Scanner(System.in);
    private boolean playerIsX;
    int minimaxMode;


    public TicTacToeGame(int minimaxMode) {
        this.minimaxMode = minimaxMode;
        this.state = new TicTacToeGameState(3);
        this.playerIsX = pickTicTacToePlayerChar();
    }

    // Demo mode constructor BE VERY CAREFUL!
    // yes having a seperate constructor is fragile and dumb
    public TicTacToeGame() {
        this.state = new TicTacToeGameState();
        this.playerIsX = true;
    }

    public void run() {
        if (minimaxMode == Minimax.AB_LIMITED | minimaxMode == Minimax.MINIMAX_LIMITED) {
            minimax = new Minimax<>(MinimaxSetupHelper.pickDepth(), minimaxMode);
        } else {
            minimax = new Minimax<>(minimaxMode);
        }
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println("You are " + (playerIsX ? "X" : "O") + " and " +
                (playerIsX ? "go first" : "go second"));

        clearScreen();
        state.displayGameState();

        while (!state.isTerminal()) {
            int currentPlayer = state.getCurrentPlayer();
            boolean isPlayerTurn = (currentPlayer == X_PLAYER && playerIsX) ||
                    (currentPlayer == O_PLAYER && !playerIsX);

            if (isPlayerTurn) {
                playerMove();
            } else {
                aiMove();
            }

            clearScreen();
            state.displayGameState();
        }

        printResult();
    }

    private void playerMove() {
        System.out.println("Your turn (" + (state.getCurrentPlayer() == X_PLAYER ? "X" : "O") + ")");
        int row, col;
        boolean validMove = false;

        do {
            row = getValidInput("Enter row (1-3): ");
            col = getValidInput("Enter column (1-3): ");

            TicTacToeMove move = new TicTacToeMove(row, col, state.getCurrentPlayer());

            if (state.getValidMoves().contains(move)) {
                state.applyMove(move);
                validMove = true;
                System.out.println("You played: " + move);
            } else {
                System.out.println("Invalid move. That spot is taken or out of bounds. Try again.");
            }
        } while (!validMove);
    }

    private int getValidInput(String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                // adjust from human friendly 1 based indexing to 0 based indexing
                if (input >= 0 && input <= state.BOARD_SIZE) {
                    scanner.nextLine(); // clear newline
                    input--;
                    return input;
                } else {
                    System.out.println("Input out of bounds! Enter a number between 1 and 3.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // clear invalid input
            }
        }
    }

    private void aiMove() {
        System.out.println("AI is thinking...");
        boolean aiIsMaximizing = state.getCurrentPlayer() == X_PLAYER;
        TicTacToeMove bestMove = minimax.getBestMove(state, aiIsMaximizing);
        System.out.println("AI plays: " + bestMove);
        state.applyMove(bestMove);
    }

    private void printResult() {
        int winner = state.getWinner();
        if (winner == 0) {
            System.out.println("Game over! It's a draw!");
        } else {
            boolean playerWon = (winner == X_PLAYER && playerIsX) ||
                    (winner == O_PLAYER && !playerIsX);
            System.out.println("Game over! " + (playerWon ? "You win!" : "AI wins!"));
        }
    }

    private void printDemoResult() {
        // dumb workaround cus printResult assumes human
        int winner = state.getWinner();
        if (winner == 0) {
            System.out.println("Game over! It's a draw!");
        } else {
            char victorChar = (winner == X_PLAYER) ? 'X' : 'O';
            System.out.println("The winner is " + victorChar);
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static boolean pickTicTacToePlayerChar() {
        String input;
        boolean playerIsX = false;
        while (true) {
            System.out.print("Do you want to play as X (goes first) or O (goes second)? ");
            input = scanner.nextLine().toUpperCase();
            if (input.equals("X")) {
                playerIsX = true;
                break;
            } else if (input.equals("O")) {
                playerIsX = false;
                break;
            } else {
                System.out.println("Invalid input. Please enter X or O.");
            }
        }
        return playerIsX;
    }

    private void playDemoTurn(Minimax<TicTacToeMove, TicTacToeGameState> m, boolean random) {
        System.out.println("AI is thinking...");
        boolean aiIsMaximizing = state.getCurrentPlayer() == X_PLAYER;
        TicTacToeMove move = m.getBestMove(state, aiIsMaximizing);
        System.out.println("AI plays: " + move);
        state.applyMove(move);
    }

    // Testing shenanigans
    // player is x, ai is o
    LocalDateTime timestamp = LocalDateTime.now();
    String gamePrefix = "scalability";

    MemoryTracker xAiMemoryTracker = new MemoryTracker();
    MemoryTracker oAiMemoryTracker = new MemoryTracker();
    CSVWriter<MemoryTracker> xAiMemoryTrackerWriter = new CSVWriter<>("tictactoe " + gamePrefix + " xAi memory", timestamp);
    CSVWriter<MemoryTracker> oAiMemoryTrackerWriter = new CSVWriter<>("tictactoe " + gamePrefix + " oAi memory", timestamp);

    DurationTracker xAiDurationTracker = new DurationTracker();
    DurationTracker oAiDurationTracker = new DurationTracker();
    CSVWriter<DurationTracker> xAiDurationTrackerWriter = new CSVWriter<>("tictactoe " + gamePrefix + " xAi duration", timestamp);
    CSVWriter<DurationTracker> oAiDurationTrackerWriter = new CSVWriter<>("tictactoe " + gamePrefix + " oAi duration", timestamp);

    WinTracker winTracker = new WinTracker("xAi_wins", "oAi_wins");
    CSVWriter<WinTracker> winTrackerCSVWriter = new CSVWriter<>("tictactoe " + gamePrefix + " wins", timestamp);

    // --- end test shenanigans

    public void demo() {
        for (int boardsize = 3; boardsize < 45; boardsize += 2) {
            for (int round = 1; round < 3; round++) {
                this.state = new TicTacToeGameState(boardsize);
                this.playerIsX = true;

                Minimax<TicTacToeMove, TicTacToeGameState> xAi;
                Minimax<TicTacToeMove, TicTacToeGameState> oAi;

                xAi = new Minimax<>(Minimax.AB_COMPLETE);
                oAi = new Minimax<>(Minimax.RANDOM);
                // pick ai 1
                //        System.out.println("Pick mode for X");
                //        int xAiType = MinimaxSetupHelper.pickAiType();
                //        if (xAiType == Minimax.AB_LIMITED || xAiType == Minimax.MINIMAX_LIMITED) {
                //            int xDifficulty = MinimaxSetupHelper.pickDepth();
                //            xAi = new Minimax<>(xAiType, xDifficulty);
                //        } else if (xAiType == 5) {
                //            xAi = new Minimax<>(0);
                //        } else {
                //            xAi = new Minimax<>(xAiType);
                //        }
                //
                //        // pick ai 2
                //        int oAiType = MinimaxSetupHelper.pickAiType();
                //        if (oAiType == Minimax.AB_LIMITED || oAiType == Minimax.MINIMAX_LIMITED) {
                //            int oDifficulty = MinimaxSetupHelper.pickDepth();
                //            oAi = new Minimax<>(oAiType);
                //        } else {
                //            oAi = new Minimax<>(xAiType);
                //        }

                state.displayGameState();
                // play game
                while (true) {
                    if (state.isTerminal()) {
                        int winner = state.getWinner();
                        if (winner == 0) {
                            // draw: do nothing or implement draw tracking
                            System.out.println("Demo game ended in a draw.");
                        } else if (winner == X_PLAYER) {
                            winTracker.playerWins(); // X AI
                        } else if (winner == O_PLAYER) {
                            winTracker.aiWins(); // O AI
                        }
                        break;
                    }

                    xAiMemoryTracker.startTracking();
                    xAiDurationTracker.startTracking();
                    playDemoTurn(xAi, false);
                    xAiDurationTracker.stopTracking();
                    xAiMemoryTracker.stopTracking();
                    state.displayGameState();

                    //            try {
                    //                Thread.sleep(500);
                    //            } catch (InterruptedException ie) {
                    //                Thread.currentThread().interrupt();
                    //            }
                    if (state.isTerminal()) {
                        int winner = state.getWinner();
                        if (winner == 0) {
                            // draw: do nothing or implement draw tracking
                            System.out.println("Demo game ended in a draw.");
                        } else if (winner == X_PLAYER) {
                            winTracker.playerWins(); // X AI
                        } else if (winner == O_PLAYER) {
                            winTracker.aiWins(); // O AI
                        }
                        break;
                    }

//                oAiMemoryTracker.startTracking();
//                oAiDurationTracker.startTracking();
                    playDemoTurn(oAi, false);
//                oAiDurationTracker.stopTracking();
//                oAiMemoryTracker.stopTracking();
                    state.displayGameState();
                    //            try {
                    //                Thread.sleep(500);
                    //            } catch (InterruptedException ie) {
                    //                Thread.currentThread().interrupt();
                    //            }
                    break;
                }
                System.out.println("The Game is over");
                printDemoResult();
            }
            try {
                xAiMemoryTrackerWriter.writeRun(xAiMemoryTracker);
                //            oAiMemoryTrackerWriter.writeRun(oAiMemoryTracker);
                xAiDurationTrackerWriter.writeRun(xAiDurationTracker);
                //            oAiDurationTrackerWriter.writeRun(oAiDurationTracker);
                xAiMemoryTracker.resetTracker();
                //            oAiMemoryTracker.resetTracker();
                xAiDurationTracker.resetTracker();
                //            oAiDurationTracker.resetTracker();
                //            winTrackerCSVWriter.writeRun(winTracker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        Game game = new TicTacToeGame(Minimax.AB_COMPLETE);
//        game.run();
        Game game = new TicTacToeGame();
        game.demo();
    }
}
