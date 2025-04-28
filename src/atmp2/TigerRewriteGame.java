package atmp2;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Stack;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class TigerRewriteGame {
    public static Minimax<TigerVsDogsMove, TigerGameStateRewrite> minimax;
    public TigerGameStateRewrite state;
    public static Scanner scanner = new Scanner(System.in);
    public static Random rand = new Random();
    boolean humanPlaysAsTiger;
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

    public TigerRewriteGame() {
        setup();
        state = new TigerGameStateRewrite();

    }

    public void test() {
        display(state);
        TigerVsDogsMove tMove = humanTigerMove();
        state.applyMove(tMove);
        display(state);
        TigerVsDogsMove dMove = humanDogMove();
        state.applyMove(dMove);
        display(state);
        state.undoMove(dMove);
        display(state);


    }

    public static void display(TigerGameStateRewrite state) {
        List<String> displayStrings = new ArrayList<>();
        for(int i=0; i< state.board.length ;i++){
            for (int j=0; j< state.board.length ;j++){
                displayStrings.add(formatCellContent(i,j,state.board));
            }
        }

        System.out.println("Dogs Eaten: " + state.numEaten);
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
                displayStrings.get(0), displayStrings.get(1), displayStrings.get(2), displayStrings.get(3), displayStrings.get(4),
                displayStrings.get(5), displayStrings.get(6), displayStrings.get(7), displayStrings.get(8), displayStrings.get(9),
                displayStrings.get(10), displayStrings.get(11), displayStrings.get(12), displayStrings.get(13), displayStrings.get(14),
                displayStrings.get(15), displayStrings.get(16), displayStrings.get(17), displayStrings.get(18), displayStrings.get(19),
                displayStrings.get(20), displayStrings.get(21), displayStrings.get(22), displayStrings.get(23), displayStrings.get(24));
    }


    private static String formatCellContent(int firstIndex, int secondIndex, int[][] board) {
        return switch (board[firstIndex][secondIndex]) {
            case TigerGameStateRewrite.DOG -> String.format("%2d", firstIndex*5+secondIndex); // Dog with ID (padded)
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
    

    public void run() {
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
        // display(state);
        System.out.println("GAME OVER");

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

        System.out.println("Available Directions: ");
        for (Map.Entry<Character, Integer> entry : directionKeyMap.entrySet()) {
            char key = entry.getKey();
            int directionNumber = entry.getValue();

            if (directions.contains(directionNumber)) {
                System.out.println(key + ": " + directionNames.get(directionNumber));
            }
        }

        // Get the user's choice
        char choice = ' ';
        while (true) {
            System.out.print("Enter a direction (Q, W, E, A, S, D, Z, X, C): ");
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
        // Choose whether human plays as Tiger, Dog, or Random
        while (true) {
            System.out.print("Do you want to play as (T)iger, (D)og, or let the game choose randomly? [T/D/R]: ");
            String sideInput = scanner.nextLine().trim().toUpperCase();
            switch (sideInput) {
                case "T":
                    humanPlaysAsTiger = true;
                    System.out.println("You are playing as the Tiger!");
                    break;
                case "D":
                    humanPlaysAsTiger = false;
                    System.out.println("You are playing as the Dogs!");
                    break;
                case "R":
                    boolean randomBool = rand.nextBoolean();
                    humanPlaysAsTiger = randomBool;
                    System.out.println(randomBool ? "You are playing as the Tiger!" : "You are playing as the Dogs!");
                    break;
                default:
                    System.out.println("❌ Invalid input. Please enter T for Tiger, D for Dog, or R for Random.");
                    continue; // Ask the question again if the input is invalid
            }
            // After selecting the side (Tiger/Dog/Random), set the Minimax depth
            while (true) {
                System.out.print("Enter the Minimax depth (default is 8): ");
                String depthInput = scanner.nextLine().trim();
                if (depthInput.isEmpty()) {
                    minimax = new Minimax<>(8);
                    System.out.println("Using default Minimax depth of 8.");
                    return; // Exit the method after setting the depth
                }
                try {
                    int depth = Integer.parseInt(depthInput);
                    if (depth <= 0) {
                        System.out.println("❌ Minimax depth must be a positive number. Please try again.");
                    } else {
                        minimax = new Minimax<>(depth);
                        System.out.println("Minimax depth set to " + depth + ".");
                        return; // Exit the method after setting the depth
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input. Please enter a valid integer for depth.");
                }
            }
        }
    }

    public static void main(String[] args) {
        TigerRewriteGame g = new TigerRewriteGame();
        g.run();

    }
}
