package atmp2;

import java.util.Scanner;

import atmp2.Minimax;

public class CoinGame implements Game {
    private final Scanner scanner = new Scanner(System.in);
    private Minimax<CoinGameMove, CoinGameState> minmax;
    private MemoryTracker memoryTracker = new MemoryTracker();
    private int mode;
    private CoinGameState state;

    public CoinGame(int mode) {
        this.mode = mode;
    }

    // constructor for demo mode yes it does nothing
    public CoinGame() {
    }

    @Override
    public void run() {
        System.out.println("Welcome to the Coin Game");
        state = initializeGame();
        playGame(state);
    }

    private CoinGameState initializeGame() {
        boolean playerStarts = getPlayerStartChoice();
        int chosenStartPlayer = playerStarts ? CoinGameState.PLAYER_HUMAN : CoinGameState.PLAYER_AI;

        CoinGameState state = new CoinGameState(chosenStartPlayer, 20);

        int minmaxDepth = -1;
        if (mode == Minimax.AB_LIMITED | mode == Minimax.MINIMAXLIMITED) {

            minmaxDepth = getMinMaxDepth();
        }
        minmax = new Minimax<>(minmaxDepth, mode);

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
            if (state.isTerminal())
                break;
            clearScreen();
            displayState();
            if (state.getCurrentPlayer() == CoinGameState.PLAYER_HUMAN) {
                state.applyMove(getPlayerMove(state));
            } else {
                handleAIMove(state);
            }

            System.out.println("----------");
            System.out.println("You: " + state.getPlayerScore() + " AI:" + state.getAiScore());

        }

        boolean playerWins = state.getWinner() == CoinGameState.PLAYER_HUMAN;

        if (playerWins) {
            System.out.println("Congratulations! You won!");

        } else {
            System.out.println("You lost...");
        }
        System.out.println("You: " + state.getPlayerScore() + " AI:" + state.getAiScore());

        memoryTracker.printStats();
    }

    private CoinGameMove getPlayerMove(CoinGameState state) {
        while (true) {
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
        memoryTracker.startTracking();
        CoinGameMove bestMove = minmax.getBestMove(state, true);
        memoryTracker.stopTracking();
        System.out.println("AI plays: " + bestMove);
        state.applyMove(bestMove);
    }

    public static void main(String[] args) {
        CoinGame game = new CoinGame(Minimax.AB_LIMITED);
        game.run();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void displayState() {
        clearScreen();
        System.out.println("─".repeat(state.toString().length()));
        System.out.println(state);
        System.out.println("─".repeat(state.toString().length()));
        System.out.println("You: " + state.getPlayerScore() + " AI:" + state.getAiScore());
        System.out.println("Will you pick from the left or right? (l/r)");
    }

    @Override
    public void demo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'demo'");
    }
}
