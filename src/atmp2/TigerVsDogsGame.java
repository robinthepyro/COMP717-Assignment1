// // package atmp2;

// // import java.util.Scanner;

// // import atmp2.TigerVsDogsGameState;
// // import atmp2.TigerVsDogsMove;

// // import java.util.List;
// // import java.util.Random;

// // public class TigerVsDogsGame {
// //     public static final int AI_PLAYER = 1;
// //     public static final int HUMAN_PLAYER = 2;

// //     private static final Scanner scanner = new Scanner(System.in);
// //     private static Minimax<NimMove, NimGameState> minimax;

// //     public static void main(String[] args) {
// //         System.out.println("Welcome To Tiger Vs Dogs");
// //         TigerVsDogsGameState state = new TigerVsDogsGameState();
// //         int[] nums = { 1, 2, 4, 14, 15 };

// //         printBoard(state);

// //         while (!state.isTerminal()) {
// //             state.displayGame();
// //             TigerVsDogsMove move = getPlayerMove(state);
// //             state.applyMove(move);
// //             state.displayGame();
// //             move = getRandomDogMove(state);
// //             state.applyMove(move);

// //             int[] numbers = { 0, 0, 0, 0, 0 };
// //             printRow(numbers, 0, 5);

// //         }
// //         // state.displayGame();
// //         // System.out.println(state.getValidMoves());
// //         // state.applyMove(getPlayerMove(state));
// //         // printBoard(state);

// //     }

// //     // TODO improve the input handling to accept full written words too
// //     // this code stinks so much... gross ambiguous, fragile garbage
// //     private static TigerVsDogsMove getPlayerMove(TigerVsDogsGameState state) {
// //         List<TigerVsDogsMove> validMoves = state.getValidMoves();
// //         if (state.tigerTurn) {
// //             System.out.println("Pick a Direction");
// //             System.out.println(
// //                     "Left[a] | Right[d] | Up[w] | Down[s] | Down-Left[z] | Down-Right[c] | Up-Left[q] | Up-Right[e]");
// //             String direction = scanner.next();
// //             if (direction.compareTo("q") == 0) {
// //                 return createMove(state.getTigerPosition(), -1, -1);
// //             } else if (direction.compareTo("w") == 0) {
// //                 return createMove(state.getTigerPosition(), -1, 0);
// //             } else if (direction.compareTo("e") == 0) {
// //                 return createMove(state.getTigerPosition(), -1, 1);
// //             } else if (direction.compareTo("a") == 0) {
// //                 return createMove(state.getTigerPosition(), 0, -1);
// //             } else if (direction.compareTo("d") == 0) {
// //                 return createMove(state.getTigerPosition(), 0, 1);
// //             } else if (direction.compareTo("s") == 0) {
// //                 return createMove(state.getTigerPosition(), 1, 0);
// //             } else if (direction.compareTo("z") == 0) {
// //                 return createMove(state.getTigerPosition(), 1, -1);
// //             } else if (direction.compareTo("c") == 0) {
// //                 return createMove(state.getTigerPosition(), 1, 1);
// //             }

// //         }

// //         return new TigerVsDogsMove(0, 6);
// //     }

// //     // janky method that has the power to break a lot of stuff. Don't depend on it
// //     // anywhere pls
// //     private static TigerVsDogsMove createMove(int startNode, int vertical, int horizontal) {
// //         int endNode = startNode;
// //         endNode += (vertical * 5);
// //         endNode += horizontal;
// //         return new TigerVsDogsMove(startNode, endNode);

// //     }

// //     // print numbers from 0 to 99 in pretty box
// //     private static void printRow(int[] nums, int rangeStart, int rangeEnd) {
// //         String row = "";
// //         System.out.print("╭───╮ ╭───╮ ╭───╮ ╭───╮ ╭───╮\n");
// //         for (int i = rangeStart; i < rangeEnd; i++) {
// //             int num = nums[i];
// //             if (i < 4) {
// //                 if (num > 10) {
// //                     System.out.print("│" + num + " │─");
// //                 } else {
// //                     System.out.print("│ " + num + " │─");
// //                 }
// //             } else {
// //                 if (num > 10) {
// //                     System.out.print("│" + num + " │");
// //                 } else {
// //                     System.out.print("│ " + num + " │");
// //                 }

// //             }
// //         }
// //         System.out.println();
// //         System.out.print("╰───╯ ╰───╯ ╰───╯ ╰───╯ ╰───╯\n");

// //     }

// //     private static void printBoard(TigerVsDogsGameState state) {

// //         char[] displayChars = new char[state.board.length];
// //         for (int i = 0; i < displayChars.length; i++) {
// //             System.out.println(state.board[i]);
// //             switch (state.board[i]) {
// //                 case 1: {
// //                     displayChars[i] = 'D';
// //                     break;
// //                 }
// //                 case -3: {
// //                     displayChars[i] = 'T';
// //                     break;
// //                 }
// //                 case 0: {
// //                     displayChars[i] = ' ';
// //                     break;
// //                 }
// //             }
// //         }
// //         System.out.printf("""
// //                 ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
// //                 │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
// //                 ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
// //                   |   ＼  |   ／  |   ＼  |   ／  |
// //                 ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
// //                 │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
// //                 ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
// //                   |   ／  |   ＼  |   ／  |   ＼  |
// //                 ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
// //                 │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
// //                 ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
// //                   |   ＼  |   ／  |   ＼  |   ／  |
// //                 ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
// //                 │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
// //                 ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
// //                   |   ／  |   ＼  |   ／  |   ＼  |
// //                 ╭───╮   ╭───╮   ╭───╮   ╭───╮   ╭───╮
// //                 │ %s │———│ %s │———│ %s │———│ %s │———│ %s │
// //                 ╰───╯   ╰───╯   ╰───╯   ╰───╯   ╰───╯
// //                 """,
// //                 displayChars[0],
// //                 displayChars[1],
// //                 displayChars[2],
// //                 displayChars[3],
// //                 displayChars[4],
// //                 displayChars[5],
// //                 displayChars[6],
// //                 displayChars[7],
// //                 displayChars[8],
// //                 displayChars[9],
// //                 displayChars[10],
// //                 displayChars[11],
// //                 displayChars[12],
// //                 displayChars[13],
// //                 displayChars[14],
// //                 displayChars[15],
// //                 displayChars[16],
// //                 displayChars[17],
// //                 displayChars[18],
// //                 displayChars[19],
// //                 displayChars[20],
// //                 displayChars[21],
// //                 displayChars[22],
// //                 displayChars[23],
// //                 displayChars[24]);

// //     }

// //     private static TigerVsDogsMove getRandomDogMove(TigerVsDogsGameState state) {
// //         Random rand = new Random();
// //         List<TigerVsDogsMove> v = state.getOptimisedValidMoves();
// //         return v.get(rand.nextInt(v.size()));

// //     }

// // }
// //
// //
// //
// package atmp2;

// import java.util.List;

// public class TigerVsDogsGame {

//     private TigerVsDogsGameState gameState;

//     public TigerVsDogsGame() {
//         this.gameState = new TigerVsDogsGameState();
//     }

//     public void displayGame() {
//         System.out.println("Current Game State:");
//         System.out.println(gameState.printBoard());
//     }

//     // Simulate a move in the game
//     public void makeMove(TigerVsDogsMove move) {
//         System.out.println("Making move: " + move);
//         gameState.applyMove(move);
//         gameState.displayGame();
//     }

//     // Run a simple test where we make a few moves
//     public void runTest() {
//         // Display initial state
//         gameState.displayGame();

//         // Get valid moves for the current state
//         List<TigerVsDogsMove> validMoves = gameState.getValidMoves();
//         System.out.println("Valid Moves: " + validMoves);

//         // Example of applying a valid move
//         if (!validMoves.isEmpty()) {
//             makeMove(validMoves.get(0));  // Apply first valid move for simplicity
//         }

//         // Example of another valid move after the tiger's turn
//         validMoves = gameState.getValidMoves();
//         if (!validMoves.isEmpty()) {
//             makeMove(validMoves.get(0));  // Apply next valid move
//         }
//     }

//     // Entry point to run the game test
//     public static void main(String[] args) {
//         TigerVsDogsGame game = new TigerVsDogsGame();
//         game.runTest();
//     }
// }

package atmp2;

import java.util.List;
import java.util.Scanner;

public class TigerVsDogsGame {
    private final Scanner scanner = new Scanner(System.in);

    public void playGame() {
        TigerVsDogsGameState gameState = new TigerVsDogsGameState();
        Minimax<TigerVsDogsMove, TigerVsDogsGameState> minimax = new Minimax<>(5); // Set depth as needed

        System.out.println("Welcome to Tiger vs Dogs!");
        boolean humanIsTiger = askIfHumanIsTiger();

        boolean tigerTurn = true; // Tiger always starts
        while (!gameState.isTerminal()) {
            System.out.println("\nCurrent Game State:");
            System.out.println(gameState); // Ensure this prints a readable board

            boolean humanTurn = (tigerTurn && humanIsTiger) || (!tigerTurn && !humanIsTiger);

            if (humanTurn) {
                System.out.println("Your move (" + (tigerTurn ? "Tiger" : "Dog") + ")");
                List<TigerVsDogsMove> validMoves = gameState.getValidMoves();
                printMoves(validMoves);
                int moveIndex = getUserMoveIndex(validMoves.size());
                TigerVsDogsMove chosenMove = validMoves.get(moveIndex);
                gameState.applyMove(chosenMove);
            } else {
                System.out.println("AI is thinking...");
                TigerVsDogsMove aiMove = minimax.getBestMove(gameState, !humanIsTiger);
                System.out.println("AI plays: " + aiMove);
                gameState.applyMove(aiMove);
            }

            tigerTurn = !tigerTurn;
        }

        System.out.println("\nGame Over!");
        int score = gameState.evaluate();
        if (score > 0) {
            System.out.println("Tiger wins!");
        } else if (score < 0) {
            System.out.println("Dogs win!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    private boolean askIfHumanIsTiger() {
        while (true) {
            System.out.print("Do you want to play as the Tiger? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y"))
                return true;
            if (input.equals("n"))
                return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    private void printMoves(List<TigerVsDogsMove> moves) {
        for (int i = 0; i < moves.size(); i++) {
            System.out.println(i + ": " + moves.get(i));
        }
    }

    private int getUserMoveIndex(int maxIndex) {
        while (true) {
            System.out.print("Enter move number: ");
            try {
                int index = Integer.parseInt(scanner.nextLine());
                if (index >= 0 && index < maxIndex) {
                    return index;
                }
            } catch (NumberFormatException e) {
                // Ignore and reprompt
            }
            System.out.println("Invalid input. Please enter a number between 0 and " + (maxIndex - 1));
        }
    }

    // Entry point for testing
    public static void main(String[] args) {
        TigerVsDogsGame game = new TigerVsDogsGame();
        game.playGame();
    }

    public static void printBoard(TigerVsDogsGameState state) {

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
}
