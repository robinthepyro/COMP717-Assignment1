package atmp2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import atmp2.TigerVsDogsMove;

public class TigerVsDogsGame {
    TigerVsDogsGameState state;
    Minimax<TigerVsDogsMove, TigerVsDogsGameState> minimax;

    TigerVsDogsGame() {
        this.state = new TigerVsDogsGameState();
        this.minimax = new Minimax<>(8);
    }

    public void run() {
        // gameloop
        TigerVsDogsMove move;
        while (true) {
            displayGame(state);
            move = pickTigerMove();
            state.applyMove(move);
            if (state.isTerminal()){
                System.out.println("Game Over Tiger wins");
            }
            displayGame(state);
            move = minimax.getBestMove(state, true);
            state.applyMove(move);
            if (state.isTerminal()){
                System.out.println("Game Over Dogs win");
            }

            // check terminal
            // dog turn
            // check terminal
        // }
        // declare winner
    }

    public static void displayGame(TigerVsDogsGameState state) {

        char[] displayChars = new char[state.board.length];
        for (int i = 0; i < displayChars.length; i++) {
            // System.out.println(state.board[i]);

            switch (state.board[i]) {
                case 1: {
                    displayChars[i] = 'D';
                    break;
                }
                case -3: {
                    displayChars[i] = 'T';
                    break;
                }
                case 0: {
                    displayChars[i] = ' ';
                    break;
                }
            }
        }
        System.out.printf("""
                ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
                  |   ＼  |   ／  |   ＼  |   ／  |
                ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
                  |   ／  |   ＼  |   ／  |   ＼  |
                ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
                  |   ＼  |   ／  |   ＼  |   ／  |
                ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
                  |   ／  |   ＼  |   ／  |   ＼  |
                ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
                """,
                displayChars[0],
                displayChars[1],
                displayChars[2],
                displayChars[3],
                displayChars[4],
                displayChars[5],
                displayChars[6],
                displayChars[7],
                displayChars[8],
                displayChars[9],
                displayChars[10],
                displayChars[11],
                displayChars[12],
                displayChars[13],
                displayChars[14],
                displayChars[15],
                displayChars[16],
                displayChars[17],
                displayChars[18],
                displayChars[19],
                displayChars[20],
                displayChars[21],
                displayChars[22],
                displayChars[23],
                displayChars[24]);
    }

    // method to get move if human is playing dogs
    public TigerVsDogsMove getDogMove() {
        return new TigerVsDogsMove(0, 0);
    }

    // method to get move if human is playing tiger
    public TigerVsDogsMove getTigerMove() {
        return new TigerVsDogsMove(0, 0);
    }

    public TigerVsDogsMove pickTigerMove() {
        Scanner scanner = new Scanner(System.in);
        List<Integer> validDirections = state.getValidDirections(state.getTigerPosition());
        int startPos = state.getTigerPosition();

        Map<Integer, String> directionNames = Map.of(
                1, "Up-Left", // -6
                2, "Up", // -5
                3, "Up-Right", // -4
                4, "Left", // -1
                5, "Right", // +1
                6, "Down-Left", // +4
                7, "Down", // +5
                8, "Down-Right" // +6
        );

        Map<Integer, Integer> directionOffsets = Map.of(
                1, -6,
                2, -5,
                3, -4,
                4, -1,
                5, +1,
                6, +4,
                7, +5,
                8, +6);

        for (int dir : validDirections) {
            System.out.printf("%d: %s%n", dir, directionNames.get(dir));
        }

        while (true) {
            System.out.print("Enter the number of your chosen direction: ");
            String input = scanner.nextLine().trim();

            try {
                int chosenDirection = Integer.parseInt(input);
                if (validDirections.contains(chosenDirection)) {
                    int offset = directionOffsets.get(chosenDirection);
                    int endPos = startPos + offset;
                    return new TigerVsDogsMove(startPos, endPos);
                } else {
                    System.out.println("That direction is not valid. Please choose from the list above.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number corresponding to a direction.");
            }
        }
    }

    private boolean humanStarts() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        while (true) {
            System.out.print("Would you like to start? [Yes, No, Maybe, Quit] [Ynmq]: ");

            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "yes":
                case "y":
                    return true;
                case "no":
                case "n":
                    return false;
                case "maybe":
                case "m":
                    return random.nextBoolean();
                case "quit":
                case "q":
                    System.out.println("Quitting the game. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Please enter Yes, No, Maybe, or Quit (y/n/m/q).");
            }
        }
    }

}
