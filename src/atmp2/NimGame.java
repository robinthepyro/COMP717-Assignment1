package atmp2;

import java.util.Scanner;

import atmp2.NimGameState;

import java.util.List;

public class NimGame implements Game {
    // Constants for Player types
    public static final int AI_PLAYER = 1;
    public static final int HUMAN_PLAYER = 2;
    private int mode;
    private NimGameState state;
    private boolean demoOddTurn;

    private static final Scanner scanner = new Scanner(System.in);
    private static Minimax<NimMove, NimGameState> minimax;

    public NimGame(int mode) {
        this.mode = mode;
    }

    // constructor for demo mode
    public NimGame() {
        this.demoOddTurn = true;
        this.state = new NimGameState();
    }

    @Override
    public void run() {
        System.out.println("Welcome to Misère Nim!");
        NimGameState state = initializeGame();
        playGame(state);
    }

    private NimGameState initializeGame() {
        // Player choice for first turn
        boolean playerTurn = getPlayerChoice();

        // Initializing game state
        NimGameState state = new NimGameState(playerTurn);

        // Get Minimax depth from the user
        int depth = -1;
        if (mode == Minimax.AB_LIMITED | mode == Minimax.MINIMAXLIMITED) {
            System.out.println(mode);
            depth = getMinimaxDepth();
        }
        minimax = new Minimax<>(depth, mode);

        return state;
    }

    private static boolean getPlayerChoice() {
        System.out.print("Do you want to go first? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.startsWith("y");
    }

    private static int getMinimaxDepth() {
        while (true) {
            try {
                System.out.print("Enter Minimax search depth: ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static void playGame(NimGameState state) {
        boolean playerTurn = state.getPlayer() == HUMAN_PLAYER;

        // Game loop
        while (true) {
            displayGameState(state);
            if (state.isTerminal()) {
                break; // end the game if it's terminal
            }

            if (playerTurn) {
                state.applyMove(getPlayerMove(state));
            } else {
                handleAIMove(state);
            }

            playerTurn = !playerTurn; // Switch player turn
        }

        // Final game state and result
        displayFinalResult(state, playerTurn);
    }

    private static NimMove getPlayerMove(NimGameState state) {
        List<NimMove> validMoves = state.getValidMoves();
        while (true) {
            try {
                System.out.print("Choose a pile (index): ");
                int pile = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.print("How many pips to remove: ");
                int pips = Integer.parseInt(scanner.nextLine());
                NimMove move = new NimMove(pile, pips);
                if (validMoves.contains(move))
                    return move;
                else
                    System.out.println("Invalid move. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid numbers.");
            }
        }
    }

    private static void handleAIMove(NimGameState state) {
        System.out.println("AI is thinking...");
        NimMove bestMove = minimax.getBestMove(state, true);
        System.out.println("AI plays: " + bestMove);
        state.applyMove(bestMove);
    }

    private static void displayFinalResult(NimGameState state, boolean playerTurn) {
        System.out.println("\nFinal game state:");
        clearScreen(); // clear screen
        displayGameState(state, false); // display without clearing
        if (playerTurn) {
            System.out.println("You win! AI took the last pip.");
        } else {
            System.out.println("You lose! You took the last pip.");
        }
    }

    /**
     * Displays the current Misere Nim game state with piles shown vertically.
     * Each pile is represented as a column of tokens.
     * 
     * @param state The current game state
     * @param clear Whether to clear the screen before displaying
     */
    private static void displayGameState(NimGameState state, boolean clear) {
        if (clear) {
            clearScreen();
        }

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║                      MISERE NIM                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Get the piles array
        int[] piles = state.getPiles();

        // Find the maximum pile size to determine height of display
        int maxPileSize = 0;
        for (int pile : piles) {
            if (pile > maxPileSize) {
                maxPileSize = pile;
            }
        }

        // Display pile headers with numbers
        System.out.println();
        for (int i = 0; i < piles.length; i++) {
            System.out.printf("Pile %-3d", i + 1);
        }
        System.out.println();

        // Display pile sizes
        for (int i = 0; i < piles.length; i++) {
            System.out.printf(" (%d)    ", piles[i]);
        }
        System.out.println("\n");

        // Display the tokens in each pile vertically
        for (int row = maxPileSize; row > 0; row--) {
            for (int col = 0; col < piles.length; col++) {
                // Display a token if the pile height reaches this row
                if (piles[col] >= row) {
                    System.out.print("  ●     ");
                } else {
                    System.out.print("        ");
                }
            }
            System.out.println();
        }

        // Display base line for visual clarity
        System.out.print("▀▀▀▀▀▀▀▀");
        for (int i = 1; i < piles.length; i++) {
            System.out.print("▀▀▀▀▀▀▀▀");
        }
        System.out.println("\n");
    }

    private static void displayGameState(NimGameState state) {
        // I LOOOOVE METHOD OVERLOADING
        // default to clearing screen
        displayGameState(state, true);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        NimGame g = new NimGame();
        g.demo();
    }

    public static int pickAIType(String aiName) {
        System.out.println("Select a type for " + aiName);
        System.out.println("1. depth limited alpha beta pruned minimax");
        System.out.println("2. complete alpha beta pruned minimax");
        System.out.println("3. depth limited minimax");
        System.out.println("4. complete minimax");
        System.out.println("5. random moves");
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice < 6 & choice > 0) {
                    return choice - 1;
                }
                System.out.println("Invalid Selection, input a number 1-5");
            } catch (NumberFormatException e) {
                System.out.println("Invalid Selection, input a number 1-5");
            }

        }
    }

    private void playDemoTurn(Minimax<NimMove, NimGameState> m) {
        System.out.println("AI is thinking...");
        // TODO fix the fact that one of the ai players has to be considered a HUMAN
        // player.
        boolean aiIsMaximizing = state.getPlayer() == NimGameState.HUMAN_PLAYER;
        NimMove move = m.getBestMove(state, aiIsMaximizing);
        System.out.println("AI plays: " + move);
        state.applyMove(move);
    }

    @Override
    public void demo() {
        Minimax<NimMove, NimGameState> firstAI;
        Minimax<NimMove, NimGameState> secondAI;

        // pick ai 1
        int firstAIType = pickAIType("first ai");
        if (firstAIType == Minimax.AB_LIMITED || firstAIType == Minimax.MINIMAXLIMITED) {
            // TODO fix the smell here
            // ugly ugly code, needs a refactor
            int xDifficulty = TicTacToeGame.pickDepth();
            firstAI = new Minimax<>(firstAIType, xDifficulty);
        } else {
            firstAI = new Minimax<>(firstAIType);
        }

        // pick ai 2
        int secondAITYpe = pickAIType("second ai");
        if (secondAITYpe == Minimax.AB_LIMITED || secondAITYpe == Minimax.MINIMAXLIMITED) {
            // TODO fix the smell here
            // ugly ugly code, needs a refactor
            int oDifficulty = TicTacToeGame.pickDepth();
            secondAI = new Minimax<>(secondAITYpe);
        } else {
            secondAI = new Minimax<>(firstAIType);
        }

        displayGameState(state);

        // stupid workaround to make demoturn be flipped at start of game loop
        // but must start true so we gotta set it false first
        demoOddTurn = false;
        // play game
        while (true) {
            demoOddTurn = !demoOddTurn;
            displayGameState(state);

            if (demoOddTurn) {
                playDemoTurn(firstAI);
                ;
            } else {
                playDemoTurn(secondAI);
                ;
            }

            if (state.isTerminal()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        displayGameState(state);
        System.out.println("The Game is over");
        System.out.println("The winner is " + ((demoOddTurn) ? "Player 1" : "Player 2"));
    }

}
