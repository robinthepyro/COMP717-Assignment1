// A lot of stuff in this file needs to be refactored out into a general Game class
// This Game class should be able to handle all three games that will be played.
// We then need a fucking minimax algorithm that can play all three...

package games.nim;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import games.nim.NimGameState;


public class Nim {
    private static int MAX_PLAYER = 1;
    private static int MIN_PLAYER = 2;
    NimGameState gameState;

    Nim(){
        this.gameState = new NimGameState();
    }


    public void human_turn(){
        Scanner scanner = new Scanner(System.in);
        int col;
        int count = -1 ;
        // we user getIntInputInList because the constraints are pretty dynamic
        col = getIntInputInList(scanner, "Enter a Column Number "+gameState.getNonEmptyColumns().toString(), gameState.getNonEmptyColumns());
        int current = gameState.getColumn(col);
        // we use > 0 because I don't care
        // we cap this to whatever the value of the column is (in more that one place shhhhh)
        while (count < 1){
            count = getIntInput(scanner, "Enter a number of pips to take, [1 - "+current+"]");
        }
        // cap count to current
        count = (count > current)? current : count;
        gameState.subtractFromColumn(col, count);
    }


    public int getIntInRange(Scanner scanner, String prompt, int lowerBound, int upperBound){
        int ret;
        while (true){
            System.out.println(prompt);
            try {
                ret = scanner.nextInt();
                if (ret >= lowerBound && ret <=upperBound){
                    return ret;
                }
                else {
                    System.out.printf("Invalid Selection, please pick a number between %d and %d\n", lowerBound, upperBound);
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error! Please only input integer numbes");
                scanner.next(); // consume the invalid input
            }
        }
    }

    public int getIntInputInList(Scanner scanner, String prompt, List<Integer> validOpts){
        int ret;
        do{
            ret = getIntInput(scanner, prompt);
        } while (!validOpts.contains(ret));
        return ret;
    }

    // public List<List<Integer>> getValidMoves(){
    //     // unused currently but will be great once I start doing minimax magic
    //     // example return value below
    //     // {{}, {1,2,3},{1}}
    //     // this would mean that we could take 1,2,3 from col 1, or 1 from col 2, but there are no valid moves with col 0
    //     List<Integer> validCols = getValidCols();
    //     List<List<Integer>> ret = new ArrayList<List<Integer>>();
    //     for (int i = 0;i<gameState.size(); i++){
    //         ret.add(getValidPipCount(i));
    //     }
    //     return ret;
    // }

    public int getIntInput(Scanner scanner, String prompt){
        // we trap the user in a cyclic hell until they give us an int
        int ret;
        while (true){
            try{
                System.out.println(prompt);
                ret = scanner.nextInt();
                return ret;
            }
            catch (java.util.InputMismatchException e) {
                System.out.println("Error! Please only input integer numbers");
                scanner.next(); // consume the invalid input
            }
        }
    }

    public static void realMain(){
        // putting the real main code here to allow for easy development
        // I like being able to fuck with the other main and commen
        NimGameState gameState;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Nim vs Computer (Minimax)! ");
        System.out.print("Enter the difficulty level (depth for minimax, higher means harder, e.g., 3): ");
        int depth = 3; // Default depth if input is invalid
        try {
            depth = scanner.nextInt();
            if (depth < 1) {
                System.out.println("Depth should be at least 1. Using default depth 3.");
                depth = 3;
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Using default depth 3.");
            scanner.next(); // consume the invalid input
            depth = 3;
        }
        System.out.println("Difficulty level set to depth: " + depth);
        
        // Decide who starts
        System.out.print("Who should make the first move? (1 - Computer (X), 2 - You (O)): ");
        int firstMoveChooser = 1; // Default to computer first
        try {
            firstMoveChooser = scanner.nextInt();
            if (firstMoveChooser != 1 && firstMoveChooser != 2) {
                System.out.println("Invalid choice. Computer (X) will go first by default.");
                firstMoveChooser = 1;
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Computer (X) will go first by default.");
            scanner.next(); // consume invalid input
            firstMoveChooser = 1;
        }
        gameState = new NimGameState();

        // TODO gameloop here plx
        // while (true){
           
        // }
       
    }

    public static void main(String[] args){
        // realMain();
        Nim nim = new Nim();
        NimGameState gameState = new NimGameState();
        // nim.playTurn(4, 5);
        while (!gameState.isTerminal()){
            
            System.out.println(gameState.toPrettyString());
            nim.human_turn();
        }
        System.out.println("GAME OVER");
            System.out.println(gameState.toPrettyString());
        System.out.println("Player " + gameState.currentPlayer + " Lost");
    }

}
