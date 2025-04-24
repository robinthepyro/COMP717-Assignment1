//TODO! comment this code better

package atmp2;

import java.util.Scanner;
import java.util.List;

public class NimGame {
    // Constants for Player types
    public static final int AI_PLAYER = 1;
    public static final int HUMAN_PLAYER = 2;

    private static final Scanner scanner = new Scanner(System.in);
    private static Minimax<NimMove, NimGameState> minimax;

    public static void run() {
        System.out.println("Welcome to Misère Nim!");
        NimGameState state = initializeGame();
        playGame(state);
    }

    private static NimGameState initializeGame() {
        // Player choice for first turn
        boolean playerTurn = getPlayerChoice();
        
        // Initializing game state
        NimGameState state = new NimGameState(playerTurn);
        
        // Get Minimax depth from the user
        int depth = getMinimaxDepth();
        minimax = new Minimax<>(depth);
        
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
            state.displayGameState();
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
                int pile = Integer.parseInt(scanner.nextLine());
                System.out.print("How many pips to remove: ");
                int pips = Integer.parseInt(scanner.nextLine());
                NimMove move = new NimMove(pile, pips);
                if (validMoves.contains(move)) return move;
                else System.out.println("Invalid move. Try again.");
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
        state.displayGameState();
        if (playerTurn) {
            System.out.println("You win! AI took the last pip.");
        } else {
            System.out.println("You lose! You took the last pip.");
        }
    }

}

