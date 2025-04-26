package atmp2;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import atmp2.TigerVsDogsMove;

public class TigerVsDogsGame {
    TigerVsDogsGameState state;
    Minimax<TigerVsDogsMove, TigerVsDogsGameState> minimax;
    private static final Map<Integer, Integer> directionOffsets = Map.of(
            1, -6, // up-left
            2, -5, // up
            3, -4, // up-right
            4, -1, // left
            5, +1, // right
            6, +4, // down-left
            7, +5, // down
            8, +6 // down-right
    );
    private static Random rand = new Random();

    TigerVsDogsGame() {
        this.state = new TigerVsDogsGameState();
        this.minimax = new Minimax<>(8);
    }

    public TigerVsDogsMove getAIMove() {
        return state.getOptimisedValidMoves().get(rand.nextInt(state.getOptimisedValidMoves().size()));
    }

    public static TigerVsDogsMove getDogTurn(TigerVsDogsGameState state) {
        Scanner scanner = new Scanner(System.in);
        int[] board = state.board;

        int startNode;
        while (true) {
            System.out.print("\nEnter the ID of the dog you want to move: ");
            try {
                startNode = scanner.nextInt();
                if (isDogAlive(startNode, board))
                    break;
                System.out.println("❌ That ID doesn't point to a living dog. Try again.");
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        List<Integer> validDirections = state.getEmptyNeighbours(startNode);
        if (validDirections.isEmpty()) {
            System.out.println("⚠️ That dog has no valid moves. Choose a different one.");
            return getDogTurn(state);
        }

        int chosenDirection = pickDirection(validDirections, "dog at " + startNode);
        int endNode = startNode + directionOffsets.get(chosenDirection);
        return new TigerVsDogsMove(startNode, endNode);
    }

    public static int pickDirection(List<Integer> validDirections, String pieceDescription) {
        Scanner scanner = new Scanner(System.in);

        Map<Character, Integer> directionKeyMap = Map.of(
                'Q', 1, 'W', 2, 'E', 3,
                'A', 4, 'D', 5,
                'Z', 6, 'S', 7, 'C', 8);

        Map<Integer, String> directionNames = Map.of(
                1, "Up-Left", 2, "Up", 3, "Up-Right",
                4, "Left", 5, "Right",
                6, "Down-Left", 7, "Down", 8, "Down-Right");

        System.out.println("\nAvailable directions for " + pieceDescription + ":");
        for (Map.Entry<Character, Integer> entry : directionKeyMap.entrySet()) {
            int dir = entry.getValue();
            if (validDirections.contains(dir)) {
                System.out.printf("  %c → %s%n", entry.getKey(), directionNames.get(dir));
            }
        }

        while (true) {
            System.out.print("Enter your direction key (QWEASDZC): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.length() == 1 && directionKeyMap.containsKey(input.charAt(0))) {
                int chosenDirection = directionKeyMap.get(input.charAt(0));
                if (validDirections.contains(chosenDirection)) {
                    return chosenDirection;
                } else {
                    System.out.println("❌ That direction is not valid from this position.");
                }
            } else {
                System.out.println("❌ Invalid input. Please use one of QWEASDZC.");
            }
        }
    }

    private static boolean isDogAlive(int index, int[] board) {
        return index >= 0 && index < board.length && board[index] == 1;
    }

    private static String directionName(int dir) {
        return switch (dir) {
            case 1 -> "up-left";
            case 2 -> "up";
            case 3 -> "up-right";
            case 4 -> "left";
            case 5 -> "right";
            case 6 -> "down-left";
            case 7 -> "down";
            case 8 -> "down-right";
            default -> "ayo????";
        };
    }

    public void run() {
        TigerVsDogsMove move;
        Minimax<TigerVsDogsMove, TigerVsDogsGameState> minimax = new Minimax<>(1);
        while (true) {
            displayGameWithDogIDs(state);
            System.out.println(state.getValidMoves());
            state.applyMove(minimax.getBestMove(state, true));
            if (state.isTerminal()) {
                System.out.println("Game Over");
                break;
            }
            displayGameWithDogIDs(state);
            state.applyMove(getDogTurn(state));
            if (state.isTerminal()) {
                System.out.println("Game Over");
                break;
            }
        }
        displayGameWithDogIDs(state);
        System.out.println("The winner is " + state.evaluate());
    }

    // not gonna sugarcoat it. This is hideous
    // TODO realise that all these todo messages are going to stay in the codebase
    // forever. These are never going away.
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
        System.out.println("Dogs Eaten: " + state.deadDogs);
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

    // this is so much dead code holy shit
    public static void displayGameWithDogIDs(TigerVsDogsGameState state) {
        String[] display = new String[state.board.length];
        for (int i = 0; i < state.board.length; i++) {
            display[i] = formatCellContent(i, state.board);
        }

        System.out.println("Dogs Eaten: " + state.deadDogs);
        System.out.printf("""
                ╭────╮   ╭────╮   ╭────╮   ╭────╮   ╭────╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰────╯   ╰────╯   ╰────╯   ╰────╯   ╰────╯
                  |    ＼  |    ／  |    ＼  |    ／  |
                ╭────╮   ╭────╮   ╭────╮   ╭────╮   ╭────╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰────╯   ╰────╯   ╰────╯   ╰────╯   ╰────╯
                  |    ／  |    ＼  |    ／  |    ＼  |
                ╭────╮   ╭────╮   ╭────╮   ╭────╮   ╭────╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰────╯   ╰────╯   ╰────╯   ╰────╯   ╰────╯
                  |    ＼  |    ／  |    ＼  |    ／  |
                ╭────╮   ╭────╮   ╭────╮   ╭────╮   ╭────╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰────╯   ╰────╯   ╰────╯   ╰────╯   ╰────╯
                  |    ／  |    ＼  |    ／  |    ＼  |
                ╭────╮   ╭────╮   ╭────╮   ╭────╮   ╭────╮
                │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
                ╰────╯   ╰────╯   ╰────╯   ╰────╯   ╰────╯
                """,
                display[0], display[1], display[2], display[3], display[4],
                display[5], display[6], display[7], display[8], display[9],
                display[10], display[11], display[12], display[13], display[14],
                display[15], display[16], display[17], display[18], display[19],
                display[20], display[21], display[22], display[23], display[24]);
    }

    private static String formatCellContent(int index, int[] board) {
        return switch (board[index]) {
            case 1 -> String.format("%2d", index); // Dog with ID (padded)
            case -3 -> " T"; // Tiger
            case 0 -> "  "; // Empty
            default -> "??"; // Unknown
        };
    }

    // method to get move if human is playing dogs

    // method to get move if human is playing tiger
    public TigerVsDogsMove getTigerMove() {
        int startPos = state.getTigerPosition();
        List<Integer> validDirections = state.getEmptyNeighbours(startPos);

        int chosenDirection = pickDirection(validDirections, "tiger at " + startPos);
        int offset = directionOffsets.get(chosenDirection);
        int endPos = startPos + offset;

        return new TigerVsDogsMove(startPos, endPos);
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

    public static void main(String[] args) {
        TigerVsDogsGame game = new TigerVsDogsGame();
        game.run();
    }
}
