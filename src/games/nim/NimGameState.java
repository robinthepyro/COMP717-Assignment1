package games.nim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.management.monitor.GaugeMonitorMBean;

import com.sun.nio.sctp.IllegalUnbindException;


public class NimGameState {
    private static int MAX_PLAYER = 1;
    private static int MIN_PLAYER = 2;
    // private static Integer[] startState = {1,3,1};
    // 
    private static Integer[] startState = {1,3,5,7,5,3,1};

    private List<Integer> columns ;
    public int currentPlayer;
    private int colCount;

    // could I have done this differently and not used four different constructors? sure.
    // did I? nope.
    // will I change that? also nope <3
    NimGameState(){
        this.columns = Arrays.asList(startState);
        this.currentPlayer = 1;
        this.colCount = columns.size();
    }

    NimGameState(int startingPlayer){
        this.columns = Arrays.asList(startState);
        this.currentPlayer = startingPlayer;
        this.colCount = columns.size();
    }

    NimGameState(Integer[] startState){
        this.columns = Arrays.asList(startState);
        this.currentPlayer = 1;
        this.colCount = columns.size();
    }

    NimGameState(Integer[] startstate, int startingPlayer){
        this.columns = Arrays.asList(startState);
        this.currentPlayer = startingPlayer;
        this.colCount = columns.size();
    }

    public List<Integer> getColumns() {
        return columns;
    }

    public int getColCount(){
        return colCount;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getColumn(int colNum){
        // returns the number of pips in a column 
        return columns.get(colNum);
    }

    public List<Integer> getNonEmptyColumns(){
        List<Integer> ret = new ArrayList<Integer>();
        for (int i=0; i<colCount; i++){
            if (getColumn(i) > 0){
                ret.add(i);
            }
        }
        return ret;
    }


    private void setColumn(int columnIndex, int val) {
        columns.set(columnIndex, val);
    }

    public boolean isTerminal(){
        // check if game is in a terminal state (has someone won yet)
        return (Collections.max(columns) <= 0) ? true: false;
    }

    public String toPrettyString(){
        return buildPrettyString(4);
    }

    public String toPrettyString(int spacing){
        return buildPrettyString(spacing);
    }

    public void switchCurrentPlayer(){
        currentPlayer = (currentPlayer == 1)? 2:1;
    }

    public void subtractFromColumn(int col, int subtractValue) throws IllegalArgumentException {
        // this method allows updating of the actual game state (the columns of pips)
        
        // check if column number is valid
        if (col < 0 | col > getColCount()){
            throw new IllegalArgumentException("Column " + col + " does not exist.");
        }
        if (getColumn(col) <= 0){
            throw new IllegalArgumentException("Column " + col + " is empty.");
        }

        // cap subtractValue to the number of pips in chosen column
        int currentValue = getColumn(col);
        subtractValue = (subtractValue < currentValue)? subtractValue: currentValue;
        setColumn(col, currentValue-subtractValue);
    }

    private String buildPrettyString(int spacing){
        // this used to be the toString() method but I really liked having
        // an easy way to modify column spacing on the fly
        // I couldn't work out what to name this, so the name is bad
        // TODO refactor this out of NimGameState and to Nim

        // Psuedocode, because this whole method is a mess. 
        // set a threshold to the tallest columns height
        // iterate through the columns
        // if column is taller than threshold add it to string with spacing
        // else just add spacing
        // decrement threshold
        // repeat until threshold = 0
        // then add underscore and number decorations

        String ret = "";
        Integer threshold = Collections.max(this.columns);
        for(;threshold>=0;threshold--){
            for(int i=0;i<this.columns.size();i++){
                if (this.columns.get(i) > threshold){
                    ret = ret+"*";
                    for (int space_count = 1; space_count<spacing;space_count++){
                        ret = ret+" ";
                    }
                }
                else{
                    for (int space_count = 0; space_count<spacing;space_count++){
                        ret = ret+" ";
                    }
                }
            }
            ret = ret+"\n";
        }

        // underscores to seperate columns and numbers
        for(int i=0; i<this.columns.size()*spacing; i++){
            ret = ret+"_";
        }  
        ret = ret+"\n";

        // number column labels
        for(int i=0;i<this.columns.size();i++){
            ret = ret+i;
            for (int space_count = 1; space_count<spacing;space_count++){
                ret = ret+" ";
            }
        }

        // We're finally done!
        ret = ret+"\n";
        return ret;
    }

    public static void main(String[] args) {
        // TODO remove main, it's just here for testing
        NimGameState n = new NimGameState();
        System.out.println(n.toString());
    }
}
