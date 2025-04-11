package atmp2;

import java.util.Scanner;
import java.util.List;

public class NimGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Misère Nim!");
        System.out.print("Do you want to go first? (y/n): ");
        boolean playerTurn = scanner.nextLine().trim().toLowerCase().startsWith("y");

        MisereNimGameState state = new MisereNimGameState(playerTurn);

        System.out.print("Enter Minimax search depth: ");
        int depth = Integer.parseInt(scanner.nextLine());
        Minimax<NimMove, MisereNimGameState> minimax = new Minimax<>(depth);

        // Game loop
        while (true) {
            state.displayGameState();

            if (state.isGameOver()) {
                break; // break after showing the final move
            }

            if (playerTurn) {
                state.applyMove(getPlayerMove(scanner, state.getValidMoves()));
            } else {
                System.out.println("AI is thinking...");
                NimMove bestMove = minimax.getBestMove(state, true);
                System.out.println("AI plays: " + bestMove);
                state.applyMove(bestMove);
            }

            playerTurn = !playerTurn;
        }
        

        System.out.println("\nFinal game state:");
        state.displayGameState();
        System.out.println(playerTurn ? "You win! AI took the last pip." : "You lose! You took the last pip.");
        scanner.close();
    }

    private static NimMove getPlayerMove(Scanner scanner, List<NimMove> validMoves) {
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
                System.out.println("Invalid input. Please enter numbers.");
            }
        }
    }
}

