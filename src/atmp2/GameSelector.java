// TODO! factor out the TicTacToe specific code thats squatting in this class.
package atmp2;

import java.util.Scanner;

import atmp2.CoinGame;
import atmp2.TigerRewriteGame;

public class GameSelector {
    private static Scanner scanner = new Scanner(System.in);
    private Game game;
    public static void main(String[] args) {
        System.out.println("Welcome to the Game Selector!");
        boolean play = true;
        while (play) {
            System.out.println("Choose a game to play:");
            System.out.println("1. Tic Tac Toe");
            System.out.println("2. Nim Game");
            System.out.println("3. Coin Game");
            System.out.println("4. Tiger Vs Dogs Game");
            System.out.println("5. Exit");
            int choice = getGameChoice();
            switch (choice) {
                case 1:
                    // Setup for Tic Tac Toe
                    boolean playerIsX = pickTicTacToePlayerChar();
                    int difficulty = pickTicTacToeDifficulty();
                    Game game = new TicTacToeGame(playerIsX, difficulty);
                    game.run();
                    break;
                case 2:
                    // Setup for Nim Game
                    game = new NimGame();
                    game.run();
                    break;
                case 3:
                    game = new CoinGame();
                    game.run();
                    break;
                case 4:
                    game = new TigerRewriteGame();
                    game.run();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;  // Exit the program
                default:
                    System.out.println("Invalid choice. Please select a valid game.");
                    break;
            }
            play = playAgain();

        }
    }

    public static boolean playAgain(){
        boolean valid = false;
        boolean choice = false;
        String input = "";
        while(!valid){
            System.out.println("Play Another Game? [y|N]");
            input = scanner.nextLine();
            input = input.toLowerCase();

            if (input.length() == 0){
                return false;
            }
            else {
                switch(input.charAt(0)){
                    case 'y':
                        return true;
                    case 'n':
                        return false;
                    default:
                        continue;
                }

            }
        }
        return choice;
    }

    private static int getGameChoice() {
        int choice = -1;
        // TODO fix this hardcoded garbage?
        while (choice < 1 || choice > 5) {
            System.out.print("Enter your choice (1, 2, or 3 to Exit): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        }
        return choice;
    }

    private static void test(){
        System.out.println(playAgain());
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

    private static int pickTicTacToeDifficulty() {
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
