// I THINK THIS IS A DUPLICATE CLASS BUT IM SCARED TO TOUCH IT
package games.nim;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Nim {
    private static int MAX_PLAYER = 1;
    private static int MIN_PLAYER = 2;

    public List<Integer> gameState ;

    public int currentPlayer;

    Nim(){
        Integer[] startState = {1,3,1};
        // Integer[] startState = {1,3,5,7,5,3,1};
        this.gameState = Arrays.asList(startState);
        this.currentPlayer = 1;
    }

    Nim(int startingPlayer){
        Integer[] startState = {1,3,1};
        // Integer[] startState = {1,3,5,7,5,3,1};
        this.gameState = Arrays.asList(startState);
        this.currentPlayer = startingPlayer;
    }

    public int[] getBestMove(){

        int[] ret = {0,1};
        return ret;
    }

    public void playTurn(int col, int count) throws IllegalArgumentException{
        // check if args are valid
        // could maybe do input handling elsewhere
        // buuut I'm leaving this to remind me that this could fail
        if (col < 0 | col > this.gameState.size()){
            throw new IllegalArgumentException("Column number must be between 0 and 6");
        }
        // can't have negative numbers so we cap count to gameState
        // this is also ugly, and should probs be done differently
        if (count > gameState.get(col)){
            count = gameState.get(col);
        }
        gameState.set(col, gameState.get(col)-count);
        currentPlayer = (currentPlayer == 1)? 2:1;
    }

    public void printState(){
        // ugly code but it prints it nicely
        // I used way too many for loops just to add spaces
        // forgor how else to do it and am not abt to search 
        // before I lost code to me not knowing how to use lazygit there were lovely comments
        // now there are not
        int spacing = 4;
        System.out.println("Current player : "+ currentPlayer);
        Integer threshold = Collections.max(this.gameState);
        for(int h=threshold;h>=0;h--){
            for(int i=0;i<this.gameState.size();i++){
                if (this.gameState.get(i) > threshold){
                    System.out.print("*");
                    for (int space_count = 1; space_count<spacing;space_count++){
                        System.out.print(" ");
                    }
                }
                else{
                    for (int space_count = 0; space_count<spacing;space_count++){
                        System.out.print(" ");
                    }
                }
            }
            threshold--;
            System.out.println();
        }
        for(int i=0; i<this.gameState.size()*spacing; i++){
            System.out.print("_");
            
        }  
        System.out.println();
        for(int i=0;i<this.gameState.size();i++){
            System.out.print(i);
            for (int space_count = 1; space_count<spacing;space_count++){
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.println();
        System.out.println();

    }


    public void human_turn(){
        Scanner scanner = new Scanner(System.in);
        int col;
        int count;
        col = getIntInputInList(scanner, "Enter a Column Number", getValidCols());
        System.out.println(getValidPipCount(col));
        count = getIntInputInList(scanner, "Enter a pip count", getValidPipCount(col));
        playTurn(col, count);
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

    public boolean isTerminal(){
        // check if game is at terminal state
        if (Collections.max(gameState) != 0){
            return false;
        }
        return true;
    }

    public List<Integer> getValidCols(){
        // return List<Integer> of all columns that have a valid move
        // not needed for minimax? we can just call get getValidPipCount on all columns
        List<Integer> ret = new ArrayList<Integer>();
        for (int i=0; i<gameState.size(); i++){
            if (gameState.get(i) > 0){
                ret.add(i);
            }
        }
        return ret;
    }

    public List<Integer> getValidPipCount(int col) {
        // return a List<Integer> with the valid numbers of pips to remove from a given col
        List<Integer> ret = new ArrayList<Integer>();
        for (int i=1; i<=gameState.get(col);i++){
            ret.add(i);
        }
        return ret;
    }


    public List<List<Integer>> getValidMoves(){
        // unused currently but will be great once I start doing minimax magic
        // example return value below
        // {{}, {1,2,3},{1}}
        // this would mean that we could take 1,2,3 from col 1, or 1 from col 2, but there are no valid moves with col 0
        List<Integer> validCols = getValidCols();
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        for (int i = 0;i<gameState.size(); i++){
            ret.add(getValidPipCount(i));
        }
        return ret;
    }


    public void buildTree(int depth){
        // this won't be void but I actually don't know what to return rn

        // how tf do i do this??
        // uhh
        // i think im missing a node type lol
        // should
        // generate valid move, add to tree, recurse down tree to a valid move with depth--
        List<List<Integer>> validMoves = getValidMoves();


    }

    

    public int getIntInput(Scanner scanner, String prompt){
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
        Nim nim;
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
        nim = new Nim(firstMoveChooser);

        // TODO gameloop here plx
        // while (true){
           
        // }
       
    }

    public static void main(String[] args){
        // realMain();
        Nim nim = new Nim();
        // nim.playTurn(4, 5);
        while (!nim.isTerminal()){
            
            nim.printState();
            nim.human_turn();

        }
        System.out.println("terminal state reached");
        nim.printState();
        System.out.println("Player " + nim.currentPlayer + " Lost");
    }

}
