// It is 4am... Why am i still working on this.
// i need sleep
package atmp2;

import java.util.Scanner;

public class GameSelector {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Game Selector!");
        
        while (true) {
            System.out.println("Choose a game to play:");
            System.out.println("1. Tic Tac Toe");
            System.out.println("2. Nim Game");
            System.out.println("3. Exit");

            int choice = getGameChoice();

            switch (choice) {
                case 1:
                    // Setup for Tic Tac Toe
                    boolean playerIsX = getPlayerChoice();
                    int difficulty = getDifficultyLevel();
                    TicTacToeGame ticTacToeGame = new TicTacToeGame(playerIsX, difficulty);
                    ticTacToeGame.run();
                    break;
                case 2:
                    // Setup for Nim Game
                    NimGame nimGame = new NimGame();
                    nimGame.run();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;  // Exit the program
                default:
                    System.out.println("Invalid choice. Please select a valid game.");
                    break;
            }
        }
    }

    private static int getGameChoice() {
        int choice = -1;
        while (choice < 1 || choice > 3) {
            System.out.print("Enter your choice (1, 2, or 3 to Exit): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        }
        return choice;
    }

    private static boolean getPlayerChoice() {
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

    private static int getDifficultyLevel() {
        int difficulty = -1;
        while (difficulty < 1 || difficulty > 10) {
            System.out.print("Enter AI difficulty (1-10): ");
            try {
                difficulty = Integer.parseInt(scanner.nextLine());
                if (difficulty < 1 || difficulty > 10) {
                    System.out.println("Please enter a number between 1 and 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return difficulty;
    }
}
