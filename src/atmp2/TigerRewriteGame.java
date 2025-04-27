package atmp2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.naming.directory.DirContext;

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


    public void test(){

        System.out.println(minimax.getBestMove(state, false));
    }

    public void run() {
        TigerVsDogsMove move;
        while (true) {
            move = humanTigerMove();
            state.applyMove(move);
            System.out.println(state.evaluate());
            if (state.isTerminal()) {
                break;
            }
            state.display();
            move = minimax.getBestMove(state, false);
            state.applyMove(move);
            System.out.println(state.evaluate());
            if (state.isTerminal()) {
                break;
            }
            state.display();
        }
        // state.display();
        System.out.println(state.numEaten);
        System.out.println("GAME OVER");

    }

    private Coord pickDog(){
        
        return new Coord(0, 0);
    }

    private TigerVsDogsMove humanDogMove() {
        TigerVsDogsMove move = state.getOptimisedValidMoves().get(rand.nextInt(state.getOptimisedValidMoves().size()));
        return move;
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
