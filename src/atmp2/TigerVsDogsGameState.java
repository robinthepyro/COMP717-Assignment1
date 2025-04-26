/**
 * 0  1  2  3  4
 * 5  6  7  8  9
 * 10 11 12 13 14
 * 15 16 17 18 19
 * 20 21 22 23 24
 */

package atmp2;

import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TigerVsDogsGameState implements GameState<TigerVsDogsMove> {
    public static int TIGER = -3;
    public static int DOG = 1;
    public static int EMPTY = 0;
    public List<Integer> diedThisTurn;

    private static final int[] dogStartPositions = { 0, 1, 2, 3, 4, 5, 9, 10, 14,
            15, 19, 20, 21, 22, 23, 24 };
    // private static final int[] dogStartPositions = { 0, 1, 2, 3, 4, 10, 11, 12,
    // 15,14, 20,20,20,20,20,20};
    private static final int tigerStartPosition = 12;

    public boolean tigerTurn;
    public List<List<Integer>> adjacency;
    public int deadDogs; // whyyyy :c
    public int[] board;

    public TigerVsDogsGameState() {
        this.adjacency = new ArrayList<>();
        this.board = new int[25];
        this.tigerTurn = true;
        this.diedThisTurn = new ArrayList<>();
        this.deadDogs = 0;

        // Initialize adjacency list and board state
        for (int i = 0; i <= 24; i++) {
            List<Integer> adj = new ArrayList<>();

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
            if (i == dogStartPositions[j]) {
                board[i] = DOG; // Dog
                j++;
            } else if (i == tigerStartPosition) {
                board[i] = TIGER; // Tiger
            }
        }
    }

    // TODO this is a lot of duplicated code from canEat()
    // should probs remove canEat() as that is kinda dumb
    public List<Integer> getDogsToEat(Integer position) {
        List<Integer> ret = new ArrayList<>();
        List<List<Integer>> lines = getLines();
        List<Integer> target = Arrays.asList(EMPTY, DOG, TIGER, DOG, EMPTY);
        for (List<Integer> line : lines) {
            if (line.contains(position)) {
                List<Integer> expandedLine = new ArrayList<>();
                expandedLine.add(EMPTY);
                for (Integer space : line) {
                    if (space == position) {
                        expandedLine.add(TIGER);

                    } else if (board[space] == TIGER) {
                        expandedLine.add(EMPTY);
                    }

                    else {
                        expandedLine.add(board[space]);

                    }
                }
                expandedLine.add(EMPTY);
                int start = Collections.indexOfSubList(expandedLine, target);
                if (start != -1) {
                    System.out.println("line =" + line);
                    ret.add(line.get(start));
                    ret.add(line.get(start + 2));

                }
            }
        }
        return ret;
    }

    // TODO fix this hardcoded garbage?
    // low priority but this is ugly
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

        return lines;
    }

    public boolean canEat(int position) {
        List<List<Integer>> lines = getLines(); // Method to get all valid lines (straight or diagonal)
        List<Integer> target = Arrays.asList(EMPTY, DOG, TIGER, DOG, EMPTY);
        for (List<Integer> line : lines) {
            if (line.contains(position)) {
                List<Integer> expandedLine = new ArrayList<>();
                expandedLine.add(EMPTY);
                for (Integer space : line) {
                    if (space == position) {
                        expandedLine.add(TIGER);

                    } else if (board[space] == TIGER) {
                        expandedLine.add(EMPTY);
                    }

                    else {
                        expandedLine.add(board[space]);

                    }

                }
                expandedLine.add(EMPTY);
                if (Collections.indexOfSubList(expandedLine, target) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getDeadDogs() {
        return deadDogs;
    }

    public boolean isTerminal() {
        if (deadDogs >= 6) {
            return true;
        }
        if (getEmptyNeighbours(getTigerPosition()).size() == 0) {
            return true;

        }
        return false;
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
            if (deadDogs >= 6) {
                return 1;
            }
            if (getEmptyNeighbours(getTigerPosition()).size() == 0) {
                return -1;

            }
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

    // nb this will modify the move passed in if it kills
    // we store the deaths inside the moves to allow easy undoing for minimaxing
    // Doing it another way would be great, but we're running out of time and this
    // was the fastest way to implement this.
    @Override
    public void applyMove(TigerVsDogsMove move) {
        swap(move.startNode, move.endNode);
        if(canEat(getTigerPosition())){
            List<Integer> eaten = getDogsToEat(getTigerPosition());
            move.setDeadDogs(eaten);
            for (Integer dogPos : eaten) {
                board[dogPos] = EMPTY;
                deadDogs++;
            }
            
        }
    }

    public void undoMove(TigerVsDogsMove move) {
        swap(move.startNode, move.endNode);
        List<Integer> dead = move.getDeadDogs();
        if (move.getDeadDogs() != null) {
            for (Integer dogPos : dead) {
                board[dogPos] = DOG;
                deadDogs--;
            }
        }
        tigerTurn = !tigerTurn;
    }

    // stupid garbahe debug method, don't use this
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            if (i % 5 == 0) {
                ret.append("\n");
            }
            ret.append(board[i]);
        }
        return ret.toString();
    }

    public boolean isAdjacent(Integer nodeIndex1, Integer nodeIndex2) {
        if (nodeIndex1 < 0 || nodeIndex1 > 24 || nodeIndex2 < 0 || nodeIndex2 > 24) {
            throw new IllegalArgumentException("Node Index provided was outside the range of 0 - 24");
        }
        return adjacency.get(nodeIndex1).contains(nodeIndex2);
    }

    public List<Integer> getAdjacent(Integer nodeIndex) {
        return adjacency.get(nodeIndex);
    }

    public boolean isGameOver() {
        return isTerminal();
    }

    public void displayGame() {
        // don't use this, use the pretty one in TigerVsDogsGame
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
        int temp = board[nodeIndex1];
        board[nodeIndex1] = board[nodeIndex2];
        board[nodeIndex2] = temp;
    }

    public List<Integer> getEmptyNeighbours(int position) throws IllegalArgumentException {
        // return a list of empty adjacent nodes
        //
        // because the order in which the adjacency list is filled is really
        // inconvienient and we don't need this method to be highly performant (it's run
        // once
        // each human turn) I'm going to do this a suboptimal, but more readable way.
        //
        // This does rely on some ugly try catch shenanigans. I don't care.
        //
        Map<Integer, Integer> directionOffsets = Map.of(
                1, -6,
                2, -5,
                3, -4,
                4, -1,
                5, +1,
                6, +4,
                7, +5,
                8, +6);
        List<Integer> ret = new ArrayList<>();

        // this loop is ugly as hell, just iterate the map keys dumbass
        for (int i = 1; i < 9; i++) {
            try {
                int newPosition = position + directionOffsets.get(i);
                if (board[newPosition] == 0 & getAdjacent(position).contains(newPosition)) {
                    ret.add(i);
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
        return ret;
    }

    // testing
    public static void main(String[] args) {
        TigerVsDogsGameState g = new TigerVsDogsGameState();
        g.getLines();
    }
}
