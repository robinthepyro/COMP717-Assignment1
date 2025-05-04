package atmp2;

import java.util.Scanner;

import atmp2.CoinGameState;
import atmp2.Minimax;
import atmp2.MinimaxSetupHelper;
import atmp2.NimGame;

public class CoinGame implements Game {
    private final Scanner scanner = new Scanner(System.in);
    private Minimax<CoinGameMove, CoinGameState> minmax;
    private MemoryTracker memoryTracker = new MemoryTracker();
    private int mode;
    private CoinGameState state;
    private boolean demoOddTurn;

    public CoinGame(int mode) {
        this.mode = mode;
    }

    public CoinGame() {
        this.state = new CoinGameState(CoinGameState.PLAYER_AI, 20);
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
        if (mode == Minimax.AB_LIMITED | mode == Minimax.MINIMAX_LIMITED) {

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
            System.out.println("You: " + state.getPlayerScore() + " AI:" + state.getAiScore());
            System.out.println("Will you pick from the left or right? (l/r)");
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
        CoinGame game = new CoinGame();
        game.demo();
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
    }

    @Override
    public void demo() {
        Minimax<CoinGameMove, CoinGameState> firstAi;
        int firstAiType = NimGame.pickAIType("First Ai");
        Minimax<CoinGameMove, CoinGameState> secondAi;
        if (firstAiType == Minimax.AB_LIMITED || firstAiType == Minimax.MINIMAX_LIMITED) {
            int firstDifficulty = MinimaxSetupHelper.pickDepth();
            firstAi = new Minimax<>(firstDifficulty, firstAiType);
        } else {
            firstAi = new Minimax<>(firstAiType);
        }

        int secondAiType = NimGame.pickAIType("Second Ai");
        if (secondAiType == Minimax.AB_LIMITED || secondAiType == Minimax.MINIMAX_LIMITED) {
            int secondDifficulty = MinimaxSetupHelper.pickDepth();
            secondAi = new Minimax<>(secondDifficulty, secondAiType);
        } else {
            secondAi = new Minimax<>(secondAiType);
        }

        while (true) {
            demoOddTurn = !demoOddTurn;
            displayState();
            if (state.isTerminal()) {
                break;
            }
            if (demoOddTurn) {
                playDemoTurn(firstAi);
            } else {
                playDemoTurn(secondAi);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        displayState();

        System.out
                .println("The winner is the "
                        + ((state.getAiScore() < state.getPlayerScore()) ? "Seocnd AI" : "First AI"));
        System.out.println("First AI " + state.getAiScore() + "Second AI:" + state.getPlayerScore());

    }

    private void playDemoTurn(Minimax<CoinGameMove, CoinGameState> m) {
        System.out.println("AI is thinking...");
        // TODO fix the fact that one of the ai players has to be considered a HUMAN
        // player.
        CoinGameMove move = m.getBestMove(state, demoOddTurn);
        System.out.println("AI plays: " + move);
        state.applyMove(move);
    }
}
