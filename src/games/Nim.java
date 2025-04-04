package games;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Nim {
    public List<Integer> gameState ;
    public int currentPlayer;

    Nim(){
        Integer[] startState = {1,3,1};
        // Integer[] startState = {1,3,5,7,5,3,1};
        this.gameState = Arrays.asList(startState);
        this.currentPlayer = 1;
    }

    private List<Integer> getValidCol(){
        List<Integer> ret = new ArrayList<Integer>();
        for (int i=0; i<gameState.size(); i++){
            if (gameState.get(i) > 0){
                ret.add(i);
            }
        }
        return ret;
    }

    private List<Integer> getValidPipCount(int col) {
        List<Integer> ret = new ArrayList<Integer>();
        for (int i=1; i<=gameState.get(col);i++){
            ret.add(i);
        }
        return ret;
    }

    private List<List<Integer>> getValidMoves(){
        // unused currently but will be great once I start doing minimax magic
        List<Integer> validCols = getValidCol();
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        for (int i = 0;i<gameState.size(); i++){
            ret.add(getValidPipCount(i));
        }
        return ret;
    }

    private int getInt(Scanner scanner, String prompt){
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
        col = getIntInList(scanner, "Enter a Column Number", getValidCol());
        System.out.println(getValidPipCount(col));
        count = getIntInList(scanner, "Enter a pip count", getValidPipCount(col));
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

    public int getIntInList(Scanner scanner, String prompt, List<Integer> validOpts){
        int ret;
        do{
            ret = getInt(scanner, prompt);
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


    public static void main(String[] args){
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
