// TODO! factor out the TicTacToe specific code thats squatting in this class.
package atmp2;

import java.util.Scanner;

public class GameSelector {
    private static Scanner scanner = new Scanner(System.in);
    private Game game;

    public static void main(String[] args) {
        System.out.println("Welcome to the Game Selector!");
        boolean play = true;
        boolean humanVsAi = playerVsAi();
        while (play) {
            System.out.println("Choose a game to " + (humanVsAi ? "play" : "demo"));
            System.out.println("1. Tic Tac Toe");
            System.out.println("2. Nim Game");
            System.out.println("3. Coin Game");
            System.out.println("4. Tiger Vs Dogs Game");
            System.out.println("5. Exit");
            int choice = getGameChoice();

            // pve mode
            if (humanVsAi) {
                int mode;
                switch (choice) {
                    case 1:
                        // Setup for Tic Tac Toe
                        mode = MinimaxSetupHelper.pickAiType();
                        Game game = new TicTacToeGame(mode);
                        game.run();
                        break;
                    case 2:
                        // Setup for Nim Game
                        System.out.println("This Game Has Certain Minimax Modes Disabled to Prevent Crashes");
                        mode = MinimaxSetupHelper.pickValidAiType(NimGame.VALID_AI_MODES);
                        game = new NimGame(mode);
                        game.run();
                        break;
                    case 3:
                        mode = MinimaxSetupHelper.pickAiType();
                        game = new CoinGame(mode);
                        game.run();
                        break;
                    case 4:
                        System.out.println(
                                "This Game Has Certain Minimax Modes Disabled to Prevent Crashes");
                        mode = MinimaxSetupHelper.pickValidAiType(TVDGame.VALID_AI_MODES);
                        game = new TVDGame(mode);
                        game.run();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice. Please select a valid game.");
                        break;
                }
            } else {

                switch (choice) {
                    case 1:
                        // Setup for Tic Tac Toe
                        Game game = new TicTacToeGame();
                        game.demo();
                        break;
                    case 2:
                        // Setup for Nim Game
                        game = new NimGame();
                        game.demo();
                        break;
                    case 3:
                        game = new CoinGame();
                        game.demo();
                        break;
                    case 4:
                        game = new TVDGame();
                        game.demo();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return; // Exit the program
                    default:
                        System.out.println("Invalid choice. Please select a valid game.");
                        break;
                }
                play = playAgain();
            }
        }
    }

    public static boolean playAgain() {
        boolean valid = false;
        boolean choice = false;
        String input = "";
        while (!valid) {
            System.out.println("Play Another Game? [y|N]");
            input = scanner.nextLine();
            input = input.toLowerCase();

            if (input.length() == 0) {
                return false;
            } else {
                switch (input.charAt(0)) {
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
            System.out.print("Enter your choice (1, 2, 3, 4 or 5 to Exit): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        }
        return choice;
    }

    private static void test() {
        System.out.println(playAgain());
    }

    private static boolean playerVsAi() {
        System.out.println("Would you like to play vs AI, watch a demo, or quit? [p|d|q]");
        while (true) {
            String raw = scanner.nextLine().toLowerCase();
            if (raw.length() == 0) {
                return true;
            }
            char choice = raw.charAt(0);
            switch (choice) {
                case 'p':
                    return true;
                case 'd':
                    return false;
                case 'q':
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid Selection, please enter one of [p | d | q]");
            }
        }
    }

    private static int pickMinimaxMode() {
        while (true) {
            // int ABLIMITED = 0;
            // int ABCOMPLETE = 1;
            // int MINIMAXLIMITED = 2;
            // int MINIMAXCOMPLETE = 3;

            System.out.println("Select a mode");
            System.out.println("1. depth limited alpha beta pruned minimax");
            System.out.println("2. complete alpha beta pruned minimax");
            System.out.println("3. depth limited minimax");
            System.out.println("4. complete minimax");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice > 0 && choice < 4) {
                    return choice - 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Selection");
            }
        }
    }
}
