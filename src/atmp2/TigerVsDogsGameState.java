/**
 * 0  1  2  3  4
 * 5  6  7  8  9
 * 10 11 12 13 14
 * 15 16 17 18 19
 * 20 21 22 23 24
 */

package atmp2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import atmp2.TigerVsDogsMove;

public class TigerVsDogsGameState implements GameState<TigerVsDogsMove> {
    public static int TIGER = -3;
    public static int DOG = 1;
    public static int EMPTY = 0;
    public List<Integer> diedThisTurn;

    private static int[] dogStartPostions = { 0, 1, 2, 3, 4, 5, 9, 10, 14, 15, 19, 20, 21, 22, 23, 24 };
    private static int tigerStartPosition = 12;

    public boolean tigerTurn;
    public List<HashSet<Integer>> adjacency;
    public int deadDogs; // whyyyy :c
    public int[] board;

    public TigerVsDogsGameState() {
        this.adjacency = new ArrayList<>();
        this.board = new int[25];
        this.tigerTurn = true;
        this.diedThisTurn = new ArrayList<>();

        // Initialize adjacency list and board state
        for (int i = 0; i <= 24; i++) {
            HashSet<Integer> adj = new HashSet<>();

            boolean down = false;
            boolean up = false;
            boolean left = false;
            boolean right = false;
            boolean diag = (i % 2 == 0);

            if (i % 5 != 0)
                left = true;
            if ((i + 1) % 5 != 0)
                right = true;
            if (i > 4)
                up = true;
            if (i < 20)
                down = true;

            if (left)
                adj.add(i - 1);
            if (right)
                adj.add(i + 1);
            if (up)
                adj.add(i - 5);
            if (down)
                adj.add(i + 5);

            // Handle diagonal adjacency
            if (diag) {
                if (up && left)
                    adj.add(i - 6);
                if (up && right)
                    adj.add(i - 4);
                if (down && left)
                    adj.add(i + 4);
                if (down && right)
                    adj.add(i + 6);
            }

            adjacency.add(adj);
        }

        // Initialize the game board
        int j = 0;
        for (int i = 0; i < board.length; i++) {
            if (i == dogStartPostions[j]) {
                board[i] = DOG; // Dog
                j++;
            } else if (i == tigerStartPosition) {
                board[i] = TIGER; // Tiger
            }
        }
    }

    public List<List<Integer>> getLines() {
        List<List<Integer>> lines = new ArrayList<>();

        // add rows
        lines.add(Arrays.asList(0, 1, 2, 3, 4));
        lines.add(Arrays.asList(5, 6, 7, 8, 9));
        lines.add(Arrays.asList(10, 11, 12, 13, 14));
        lines.add(Arrays.asList(15, 16, 17, 18, 19));
        lines.add(Arrays.asList(20, 21, 22, 23, 24));

        // add columns
        lines.add(Arrays.asList(0, 5, 10, 15, 20));
        lines.add(Arrays.asList(1, 6, 11, 16, 21));
        lines.add(Arrays.asList(2, 7, 12, 17, 22));
        lines.add(Arrays.asList(3, 8, 13, 18, 23));
        lines.add(Arrays.asList(4, 9, 14, 19, 24));

        // Add diagonals too
        lines.add(Arrays.asList(0, 6, 12, 18, 24));
        lines.add(Arrays.asList(2, 8, 14));
        lines.add(Arrays.asList(10, 16, 22));
        lines.add(Arrays.asList(4, 8, 12, 16, 20));
        lines.add(Arrays.asList(2, 6, 10));
        lines.add(Arrays.asList(14, 18, 22));

        // for (List<Integer> line : lines) {
        //     for (Integer i : line) {
        //         System.out.print(i + " ");
        //     }
        //     System.out.println();

        // }
        return lines;
    }

    public void eatDogs(List<Integer> dogs) {
        // Remove the dogs from the board
        for (Integer dogPos : dogs) {
            board[dogPos] = 0; // 0 represents an empty space
            deadDogs++;
        }

        // Display updated board
        // System.out.println("Tiger eats dogs! Updated board:");
        // displayGame();
    }

    public boolean canTigerEatDogs() {
        Integer tigerPos = getTigerPosition();
        List<List<Integer>> lines = getLines(); // Method to get all valid lines (straight or diagonal)

        for (List<Integer> line : lines) {
            List<Integer> dogsOnLine = new ArrayList<>();

            // Collect dogs on this line
            for (Integer pos : line) {
                if (board[pos] == DOG) { // A dog is on this position
                    dogsOnLine.add(pos);
                }
            }

            // If there are exactly two dogs on the line, check if the tiger can eat them
            if (dogsOnLine.size() == 2) {
                Integer dog1 = dogsOnLine.get(0);
                Integer dog2 = dogsOnLine.get(1);

                // Check if the tiger is adjacent to both dogs
                if (isAdjacent(tigerPos, dog1) && isAdjacent(tigerPos, dog2)) {
                    // Check if the dogs are not adjacent to each other
                    if (!isAdjacent(dog1, dog2)) {
                        // The tiger can eat these two dogs
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public int getDeadDogs() {
        return deadDogs;
    }

    public boolean isTerminal() {
        if (tigerTurn) {
            return deadDogs == 6;
        } else {
            return tigerHasValidMoves();
        }
    }

    private boolean tigerHasValidMoves() {
        for (int adj : getAdjacent(getTigerPosition())) {
            if (adj == 0) {
                return true;
            }
        }
        return false;
    }

    public int evaluate() {
        if (isTerminal()) {
            return tigerTurn ? 1 : -1;
        }
        return 0;
    }

    public List<TigerVsDogsMove> getValidMoves() {
        List<TigerVsDogsMove> ret = new ArrayList<>();
        if (tigerTurn) {
            Integer tigerPos = getTigerPosition();
            for (Integer end : getAdjacent(tigerPos)) {
                if (board[end] == 0) {
                    ret.add(new TigerVsDogsMove(tigerPos, end));
                }
            }
        } else {
            for (int i = 0; i < board.length; i++) {
                if (board[i] == DOG) { // Dog
                    for (Integer end : getAdjacent(i)) {
                        if (board[end] == 0) {
                            TigerVsDogsMove move = new TigerVsDogsMove(i, end);
                            ret.add(move);
                        }
                    }
                }
            }
        }
        return ret;
    }

    public List<Integer> getDiedThisTurn(TigerVsDogsMove move) {
        swap(move.startNode, move.endNode);
        List<Integer> deadThisTurn = new ArrayList<>();
        // After applying the move, check if the tiger can eat any dogs
        if (!tigerTurn && canTigerEatDogs()) {
            List<Integer> dogsToEat = new ArrayList<>();
            Integer tigerPos = getTigerPosition();

            // Check which dogs are on the same line as the tiger
            for (List<Integer> line : getLines()) {
                if (line.contains(tigerPos)) {
                    List<Integer> dogsOnLine = new ArrayList<>();
                    for (Integer pos : line) {
                        if (board[pos] == DOG) {
                            dogsOnLine.add(pos);
                        }
                    }

                    // If exactly two dogs are on this line, eat them
                    if (dogsOnLine.size() == 2 && isAdjacent(tigerPos, dogsOnLine.get(0))
                            && isAdjacent(tigerPos, dogsOnLine.get(1))) {
                        if (!isAdjacent(dogsOnLine.get(0), dogsOnLine.get(1))) {
                            deadThisTurn = dogsOnLine;
                            break;
                        }
                    }
                }
            }
        }
        swap(move.startNode, move.endNode);
        return deadThisTurn;

    }

    public List<TigerVsDogsMove> getOptimisedValidMoves() {
        return getValidMoves();
    }

    public Integer getTigerPosition() throws IllegalStateException {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == TIGER) {
                return i;
            }
        }
        throw new IllegalStateException("We lost the Tiger :o");
    }

    @Override
    public void applyMove(TigerVsDogsMove move) {
        diedThisTurn = new ArrayList<>(); // reset deaths every turn
        swap(move.startNode, move.endNode);
        tigerTurn = !tigerTurn;

        // After applying the move, check if the tiger can eat any dogs
        if (!tigerTurn && canTigerEatDogs()) {
            List<Integer> dogsToEat = new ArrayList<>();
            Integer tigerPos = getTigerPosition();

            // Check which dogs are on the same line as the tiger
            for (List<Integer> line : getLines()) {
                if (line.contains(tigerPos)) {
                    List<Integer> dogsOnLine = new ArrayList<>();
                    for (Integer pos : line) {
                        if (board[pos] == DOG) {
                            dogsOnLine.add(pos);
                        }
                    }

                    // If exactly two dogs are on this line, eat them
                    if (dogsOnLine.size() == 2 && isAdjacent(tigerPos, dogsOnLine.get(0))
                            && isAdjacent(tigerPos, dogsOnLine.get(1))) {
                        if (!isAdjacent(dogsOnLine.get(0), dogsOnLine.get(1))) {
                            dogsToEat = dogsOnLine;
                            break;
                        }
                    }
                }
            }

            if (!dogsToEat.isEmpty()) {
                diedThisTurn = dogsToEat;
                eatDogs(dogsToEat); // Eat the dogs
                System.out.println("onnonononomomonomon");
                move.setDeadDogs(diedThisTurn);
                System.out.println(move.getDeadDogs());
            }
        }
    }

    public void undoMove(TigerVsDogsMove move) {
        swap(move.startNode, move.endNode);
        List<Integer> dead = move.getDeadDogs();
        if (move.getDeadDogs() !=null){
            for (Integer dogPos: dead){
                System.out.println(dogPos);
                board[dogPos] = DOG;

            }
        }
        tigerTurn = !tigerTurn;
    }

    public String printBoard() {
        String ret = "";
        for (int i = 0; i < board.length; i++) {
            if (i % 5 == 0) {
                ret = ret + "\n";
            }
            ret = ret + board[i];
        }
        return ret;
    }

    public boolean isAdjacent(Integer nodeIndex1, Integer nodeIndex2) {
        if (nodeIndex1 < 0 || nodeIndex1 > 24 || nodeIndex2 < 0 || nodeIndex2 > 24) {
            throw new IllegalArgumentException("Node Index provided was outside the range of 0 - 24");
        }
        return adjacency.get(nodeIndex1).contains(nodeIndex2);
    }

    public HashSet<Integer> getAdjacent(Integer nodeIndex) {
        return adjacency.get(nodeIndex);
    }

    public boolean isGameOver() {
        return isTerminal();
    }

    public void displayGame() {
        for (int i = 0; i < board.length; i++) {
            if (i % 5 == 0) {
                System.out.println();
            }
            if (board[i] == TIGER) {
                System.out.print("T");
            } else if (board[i] == DOG) {
                System.out.print("D");
            } else {
                System.out.print("*");
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    private void swap(Integer nodeIndex1, Integer nodeIndex2) {
        Integer temp = board[nodeIndex1];
        board[nodeIndex1] = board[nodeIndex2];
        board[nodeIndex2] = temp;
    }

    public static void main(String[] args) {
        TigerVsDogsGameState g = new TigerVsDogsGameState();
        g.getLines();
    }
}
