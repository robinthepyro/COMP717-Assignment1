package atmp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Stack;

import atmp2.Minimax;
import atmp2.TigerGameStateRewrite;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class TigerRewriteGame implements Game {
    public Minimax<TigerVsDogsMove, TigerGameStateRewrite> minimax;
    public TigerGameStateRewrite state;
    public Scanner scanner = new Scanner(System.in);
    public Random rand = new Random();
    boolean humanPlaysAsTiger;
    int mode;
    private static Map<Character, Integer> directionKeyMap = Map.of(
            'Q', 1, 'W', 2, 'E', 3,
            'A', 4, 'D', 5,
            'Z', 6, 'S', 7, 'C', 8);

    private static Map<Integer, String> directionNames = Map.of(
            1, "Up-Left", 2, "Up", 3, "Up-Right",
            4, "Left", 5, "Right",
            6, "Down-Left", 7, "Down", 8, "Down-Right");

    private static final Map<Integer, Coord> directionOffsets = Map.of(
            1, new Coord(-1, -1), // up-left
            2, new Coord(-1, 0), // up
            3, new Coord(-1, 1), // up-right
            4, new Coord(0, -1), // left
            5, new Coord(0, 1), // right
            6, new Coord(1, -1), // down-left
            7, new Coord(1, 0), // down
            8, new Coord(1, 1) // down-right
    );

    public TigerRewriteGame(int mode) {
        state = new TigerGameStateRewrite();
        this.mode = mode;
    }

    public void test() {
        setup();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void display(TigerGameStateRewrite state) {
        clearScreen();
        // Pre-format all cell contents
        String[][] formattedCells = new String[state.board.length][state.board.length];
        for (int i = 0; i < state.board.length; i++) {
            for (int j = 0; j < state.board.length; j++) {
                formattedCells[i][j] = formatCellContent(i, j, state.board);
            }
        }

        // Display game status
        System.out.println("\n══════════════════ TIGER VS DOGS ══════════════════");
        System.out.println("Dogs Eaten: " + state.numEaten);

        // Define the components of the grid display
        String horizontalLine = "│ %s │———│ %s │———│ %s │———│ %s │———│ %s │";
        String topBottom = "╭────╮   ╭────╮   ╭────╮   ╭────╮   ╭────╮";
        String bottomTop = "╰────╯   ╰────╯   ╰────╯   ╰────╯   ╰────╯";
        String diagonalDown = "  |    ＼  |    ／  |    ＼  |    ／  |";
        String diagonalUp = "  |    ／  |    ＼  |    ／  |    ＼  |";

        // Build and display the grid row by row
        for (int row = 0; row < state.board.length; row++) {
            // Top border of cells
            System.out.println(topBottom);

            // Cell contents with horizontal connections
            System.out.printf(horizontalLine + "\n",
                    formattedCells[row][0],
                    formattedCells[row][1],
                    formattedCells[row][2],
                    formattedCells[row][3],
                    formattedCells[row][4]);

            // Bottom border of cells
            System.out.println(bottomTop);

            // Skip diagonal connectors after the last row
            if (row < state.board.length - 1) {
                // Alternate between diagonal patterns
                System.out.println(row % 2 == 0 ? diagonalDown : diagonalUp);
            }
        }

        // Display game controls or legend if needed
        System.out.println("\nLegend: T = Tiger, D = Dog, · = Empty");
    }

    private static String formatCellContent(int firstIndex, int secondIndex, int[][] board) {
        return switch (board[firstIndex][secondIndex]) {
            case TigerGameStateRewrite.DOG -> String.format("%2d", firstIndex * 5 + secondIndex); // Dog with ID
                                                                                                  // (padded)
            case TigerGameStateRewrite.TIGER -> " T"; // Tiger
            case TigerGameStateRewrite.EMPTY -> "  "; // Empty
            default -> "??"; // Unknown
        };
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

    @Override
    public void run() {
        setup();
        clearScreen();
        while (true) {
            if (humanPlaysAsTiger) {
                display(state);
                TigerVsDogsMove hMove = humanTigerMove();
                state.applyMove(hMove);
                if (state.isTerminal()) {
                    break;
                }

                display(state);
                TigerVsDogsMove aMove = minimax.getBestMove(state, false);
                state.applyMove(aMove);
                if (state.isTerminal()) {
                    break;
                }
            } else {
                display(state);
                TigerVsDogsMove aMove = minimax.getBestMove(state, true);
                state.applyMove(aMove);
                if (state.isTerminal()) {
                    break;
                }

                display(state);
                TigerVsDogsMove hMove = humanDogMove();
                state.applyMove(hMove);
                if (state.isTerminal()) {
                    break;
                }

            }
        }
        display(state);
        System.out.println("GAME OVER");
        System.out.println("The Winner is " + ((state.getWinner() == TigerGameStateRewrite.TIGER) ? "Tiger" : "Dogs"));
        scanner.nextLine();

    }

    // method to let (human) dog player pick a dog to move
    private Coord pickDog() {
        Coord ret = null;
        while (true) {
            System.out.print("\nEnter the ID of the dog you want to move: ");
            int dog;
            try {
                dog = scanner.nextInt();
                ret = TigerGameStateRewrite.oneDToTwoD(dog);
                if (state.getNode(ret) == TigerGameStateRewrite.DOG)
                    break;
                System.out.println("❌ That ID doesn't point to a living dog. Try again.");
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        return ret;
    }

    private TigerVsDogsMove humanDogMove() {
        Coord start = pickDog();
        Coord direction = pickDirection(getEmptyNeighbourDirections(start));
        return new TigerVsDogsMove(start, start.add(direction));

    }

    private int coordPairAsDirection(Coord c1, Coord c2) throws IllegalArgumentException {
        if (!state.getAdjacent(c1).contains(c2)) {
            throw new IllegalArgumentException("c1" + " and " + c2 + "are not adjacent");
        }
        int colOffset = c2.col - c1.col;
        int rowOffset = c2.row - c1.row;

        Coord offset = new Coord(rowOffset, colOffset);
        for (Map.Entry<Integer, Coord> entry : directionOffsets.entrySet()) {
            Integer key = entry.getKey();
            Coord value = entry.getValue();
            if (offset.equals(value)) {
                return key;
            }
        }
        return 0;

    }

    // TODO refactor and put in game state class
    public List<Integer> getEmptyNeighbourDirections(Coord start) {
        List<Integer> ret = new ArrayList<>();
        for (Coord end : state.getAdjacent(start)) {
            if (state.getNode(end) == 0) {
                ret.add(coordPairAsDirection(start, end));
            }
        }
        return ret;
    }

    public TigerVsDogsMove humanTigerMove() {
        Coord start = state.getTigerPos();
        Coord direction = pickDirection(getEmptyNeighbourDirections(start));
        return new TigerVsDogsMove(start, start.add(direction));
    }

    private Coord pickDirection(List<Integer> directions) {
        Map<Integer, String> keyMapPrettyStrings = new HashMap<>();
        keyMapPrettyStrings.put(1, "[q] ↖"); // NW
        keyMapPrettyStrings.put(2, "[w] ↑"); // N
        keyMapPrettyStrings.put(3, "[e] ↗"); // NE
        keyMapPrettyStrings.put(4, "[a] ←"); // W
        keyMapPrettyStrings.put(5, "[d] →"); // E
        keyMapPrettyStrings.put(6, "[z] ↙"); // SW
        keyMapPrettyStrings.put(7, "[s] ↓"); // S
        keyMapPrettyStrings.put(8, "[c] ↘"); // SE

        System.out.println("Available Directions: ");
        for (Map.Entry<Character, Integer> entry : directionKeyMap.entrySet()) {
            char key = entry.getKey();
            int directionNumber = entry.getValue();

            if (!directions.contains(directionNumber)) {
                keyMapPrettyStrings.put(directionNumber, "     ");
            }
        }
        System.out.printf("    %s     %s     %s\n\n",
                keyMapPrettyStrings.get(1),
                keyMapPrettyStrings.get(2),
                keyMapPrettyStrings.get(3));

        System.out.printf("    %s               %s\n\n",
                keyMapPrettyStrings.get(4),
                keyMapPrettyStrings.get(5));
        System.out.printf("    %s     %s     %s\n\n",
                keyMapPrettyStrings.get(6),
                keyMapPrettyStrings.get(7),
                keyMapPrettyStrings.get(8));
        System.out.print("Enter a direction:");

        // Get the user's choice
        char choice = ' ';
        while (true) {
            String input = scanner.next();

            if (input.length() == 1) { // Check if the input is a single character
                choice = Character.toUpperCase(input.charAt(0)); // Convert to uppercase for case-insensitivity

                // Check if the chosen direction is in the available directions
                if (directionKeyMap.containsKey(choice) && directions.contains(directionKeyMap.get(choice))) {
                    // Return the corresponding Coord for the valid choice
                    int directionNumber = directionKeyMap.get(choice);
                    return directionOffsets.get(directionNumber);
                } else {
                    System.out.println("Invalid direction. Please choose one of the available directions.");
                }
            } else {
                System.out.println("Invalid input. Please enter a single key.");
            }
        }
    }

    public void setup() {
        boolean invalid = true;
        state = new TigerGameStateRewrite();
        while (invalid) {
            System.out.println("Would you like to play as the tiger, dogs, or a random side [t|d|R]");
            String raw = scanner.nextLine();
            if (raw.length() == 0){
                humanPlaysAsTiger = rand.nextBoolean();
                break;
            }
            char input = raw.toLowerCase().charAt(0);
            switch (input) {
                case 't':
                    // tiger
                    humanPlaysAsTiger = true;
                    invalid = false;
                    break;
                case 'd':
                    // dog
                    humanPlaysAsTiger = false;
                    invalid = false;
                    break;
                case 'r':
                    // random
                    humanPlaysAsTiger = rand.nextBoolean();
                    invalid = false;
                    break;
                default:
                    // try again
                    System.out.println("Invalid Selection, enter one of the following [t|d|R]");
            }
        }
        int depth = -1;
        System.out.printf("Enter minimax depth (default %d).\n", depth);
        String input = scanner.nextLine();
        try {
            depth = Integer.parseInt(input);

        } catch (NumberFormatException e) {
            depth = 5;
            System.out.printf("Invalid integer, using depth = %d\n", depth);
        }
        this.minimax = new Minimax<>(depth, mode);
    }

    public static void main(String[] args) {
        TigerRewriteGame g = new TigerRewriteGame(Minimax.ABCOMPLETE);
        g.run();

    }

	@Override
	public void demo() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'demo'");
	}
}
