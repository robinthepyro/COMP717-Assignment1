package atmp2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import atmp2.Coord;
import atmp2.TigerVsDogsMove;

public class TigerGameStateRewrite implements GameState<TigerVsDogsMove> {
    public int numEaten;
    public Stack<TigerVsDogsMove> history;
    public boolean tigerTurn;
    public int[][] board;
    private Map<Coord, List<Coord>> adj;
    private static final int BOARD_SIZE = 5;
    public static final int TIGER = 9;
    public static final int DOG = 1;
    public static final int EMPTY = 0;
    private static final List<Integer> EAT_PATTERN = Arrays.asList(EMPTY, DOG, TIGER, DOG, EMPTY);

    // this is the stupidest shit i have ever done. This should have been a loop but
    // idfc
    // it's 4am. i am sleep deprived and I kept breaking this (), so I just wrote it
    // out.
    // I love helix multi cursors + macros
    // its like a drug
    // once you realise its just faster to do it the stupid way u just get the itch
    // to do it dum
    private static Coord[][] LINES = {
            { new Coord(0, 0), new Coord(0, 1), new Coord(0, 2), new Coord(0, 3),
                    new Coord(0, 4) },
            { new Coord(1, 0), new Coord(1, 1), new Coord(1, 2), new Coord(1, 3),
                    new Coord(1, 4) },
            { new Coord(2, 0), new Coord(2, 1), new Coord(2, 2), new Coord(2, 3),
                    new Coord(2, 4) },
            { new Coord(3, 0), new Coord(3, 1), new Coord(3, 2), new Coord(3, 3),
                    new Coord(3, 4) },
            { new Coord(4, 0), new Coord(4, 1), new Coord(4, 2), new Coord(4, 3),
                    new Coord(4, 4) },
            // Columns
            { new Coord(0, 0), new Coord(1, 0), new Coord(2, 0), new Coord(3, 0),
                    new Coord(4, 0) },
            { new Coord(0, 1), new Coord(1, 1), new Coord(2, 1), new Coord(3, 1),
                    new Coord(4, 1) },
            { new Coord(0, 2), new Coord(1, 2), new Coord(2, 2), new Coord(3, 2),
                    new Coord(4, 2) },
            { new Coord(0, 3), new Coord(1, 3), new Coord(2, 3), new Coord(3, 3),
                    new Coord(4, 3) },
            { new Coord(0, 4), new Coord(1, 4), new Coord(2, 4), new Coord(3, 4),
                    new Coord(4, 4) },
            // Diagonals (even-numbered starting spaces)
            { new Coord(0, 0), new Coord(1, 1), new Coord(2, 2), new Coord(3, 3),
                    new Coord(4, 4) },
            { new Coord(0, 2), new Coord(1, 3), new Coord(2, 4) },
            { new Coord(2, 0), new Coord(3, 1), new Coord(4, 2) },
            { new Coord(0, 4), new Coord(1, 3), new Coord(2, 2), new Coord(3, 1),
                    new Coord(4, 0) },
            { new Coord(0, 2), new Coord(1, 1), new Coord(2, 0) },
            { new Coord(2, 4), new Coord(3, 3), new Coord(4, 2) } };

    // CONSTUCTOR
    TigerGameStateRewrite() {
        this.adj = buildAdjacencyMap(BOARD_SIZE);
        this.numEaten = 0;
        this.board = new int[][] {
                { DOG, DOG, DOG, DOG, DOG },
                { DOG, EMPTY, EMPTY, EMPTY, DOG },
                { DOG, EMPTY, TIGER, EMPTY, DOG },
                { DOG, EMPTY, EMPTY, EMPTY, DOG },
                { DOG, DOG, DOG, DOG, DOG } };
        this.history = new Stack<>();
        this.tigerTurn = true;
    }

    public Map<Coord, List<Coord>> buildAdjacencyMap(int boardSize) {
        Map<Coord, List<Coord>> map = new HashMap<>();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Coord current = new Coord(row, col);
                List<Coord> neighbors = new ArrayList<>();

                // Basic orthogonal directions
                int[][] directions = {
                        { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }
                };

                // Add diagonals for even tiles
                if ((row + col) % 2 == 0) {
                    directions = new int[][] {
                            { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
                            { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 }
                    };
                }

                // Add valid neighbors
                for (int[] d : directions) {
                    int newRow = row + d[0];
                    int newCol = col + d[1];
                    if (newRow >= 0 && newRow < boardSize && newCol >= 0 && newCol < boardSize) {
                        neighbors.add(new Coord(newRow, newCol));
                    }
                }

                map.put(current, neighbors);
            }
        }
        return map;
    }

    public List<Coord> getAdjacent(Coord coord) {
        return adj.getOrDefault(coord, Collections.emptyList());
    }

    // some duplicated code in get valid moves could be replaced with this.
    public List<Coord> getEmptyAdjacent(Coord coord) {
        List<Coord> ret = new ArrayList<>();
        List<Coord> adjacent = getAdjacent(coord);
        for (Coord c : adjacent) {
            if (getNode(c) == EMPTY) {
                ret.add(c);
            }
        }
        return ret;
    }

    public Coord getTigerPos() throws IllegalStateException {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == TIGER) {
                    return new Coord(i, j);
                }
            }
        }
        throw new IllegalStateException("We lost the Tiger :o");
    }

    public int getNode(Coord c) {
        return board[c.row][c.col];
    }

    private List<Integer> extendLine(Coord[] line) {
        List<Integer> ret = new ArrayList<>();
        ret.add(EMPTY);
        for (Coord coord : line) {
            ret.add(getNode(coord));
        }
        ret.add(EMPTY);
        return ret;
    }

    public List<Coord> getEaten() {
        List<Coord> ret = new ArrayList<>();
        for (Coord[] line : LINES) {
            for (Coord coord : line) {
                if (getNode(coord) == TIGER) {
                    List<Integer> extended = extendLine(line);
                    int start = Collections.indexOfSubList(extended, EAT_PATTERN);
                    if (start != -1) {
                        ret.add(line[start]);
                        ret.add(line[start + 2]);
                    }
                }
            }
        }
        return ret;

    }

    public boolean canEat() {
        for (Coord[] line : LINES) {
            for (Coord coord : line) {
                if (getNode(coord) == TIGER) {
                    List<Integer> extended = extendLine(line);
                    int start = Collections.indexOfSubList(extended, EAT_PATTERN);
                    if (start != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<TigerVsDogsMove> getValidMoves() {
        display();
        List<TigerVsDogsMove> ret = new ArrayList<>();
        if (tigerTurn) {
            display();
            for (Coord a : getAdjacent(getTigerPos())) {
                if (getNode(a) == EMPTY) {
                    ret.add(new TigerVsDogsMove(getTigerPos(), a));
                }
            }

        } else {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] == DOG) {
                        Coord start = new Coord(r, c);
                        for (Coord end : getAdjacent(start)) {
                            if (getNode(end) == EMPTY) {
                                ret.add(new TigerVsDogsMove(start, end));
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public List<TigerVsDogsMove> getOptimisedValidMoves() {
        return getValidMoves();
    }

    private void setNode(Coord coord, int val) {
        board[coord.row][coord.col] = val;
    }

    /**
     * Regenerates the board state from scratch by applying all moves in the history
     * stack
     * This ensures the board state and numEaten property are consistent
     */
    public void regenerateStateFromHistory() {
        // Store the current history
        Stack<TigerVsDogsMove> tempHistory = new Stack<>();
        while (!history.isEmpty()) {
            tempHistory.push(history.pop());
        }

        // Reset to initial state
        this.board = new int[][] {
                { DOG, DOG, DOG, DOG, DOG },
                { DOG, EMPTY, EMPTY, EMPTY, DOG },
                { DOG, EMPTY, TIGER, EMPTY, DOG },
                { DOG, EMPTY, EMPTY, EMPTY, DOG },
                { DOG, DOG, DOG, DOG, DOG } };
        this.numEaten = 0;
        this.tigerTurn = true;

        // Replay moves in correct order (oldest to newest)
        Stack<TigerVsDogsMove> orderedMoves = new Stack<>();
        while (!tempHistory.isEmpty()) {
            orderedMoves.push(tempHistory.pop());
        }

        while (!orderedMoves.isEmpty()) {
            TigerVsDogsMove move = orderedMoves.pop();
            // Special apply move that doesn't add to history
            applyMoveWithoutHistory(move);
            // Restore to original history
            history.push(move);
        }
    }

    /**
     * Applies a move without adding it to the history stack
     */
    private void applyMoveWithoutHistory(TigerVsDogsMove move) {
        int temp = getNode(move.startNode);
        System.out.println("TEMP == " + temp);
        display();
        setNode(move.startNode, 0);
        setNode(move.endNode, temp);
        display();

        if (canEat()) {
            move.deadDogs = getEaten();
            eat();
            numEaten += move.deadDogs.size();
        }

        // Switch turns without adding to history
        tigerTurn = !tigerTurn;
    }

    @Override
    public void applyMove(TigerVsDogsMove move) {
        if (move != null) {
            history.push(move);
            regenerateStateFromHistory();
        }
    }

    private void eat() {
        List<Coord> toEat = getEaten();
        for (Coord d : toEat) {
            setNode(d, EMPTY);
        }

    }

    public boolean equals(TigerGameStateRewrite s) {
        if (s.tigerTurn != tigerTurn) {
            System.out.println("tigerturn check failed");
            return false;
        }
        if (!Arrays.deepEquals(s.board, board)) {
            System.out.println("Board check failed");
            return false;
        }
        if (s.numEaten != numEaten) {
            System.out.println("numEaten check failed");
            return false;
        }
        return true;
    }

    @Override
    public void undoMove(TigerVsDogsMove move) {
        if (move != null) {
            history.pop();
            System.out.println("this one\n\n\n\n\\n\n\n\n\n\n\n\n\nn\n\\nn\\n\n\n");
            display();
            regenerateStateFromHistory();
        }
    }

    public int getWinner() {
        if (numEaten >= 6)
            return TIGER;
        if (tigerTurn && getValidMoves().isEmpty() && !canEat())
            return DOG;
        return 0;
    }

    @Override
    public boolean isTerminal() {
        if (numEaten >= 6) {
            System.out.println("dead dogs >= 6 tiger win");
            return true;

        }
        if (getValidMoves().isEmpty())
            return true;
        return false;
    }

    @Override
    public int evaluate() {
        int val = getWinner();
        switch (val) {
            case TIGER:
                return 100;
            case DOG:
                return -100;
            default:
                return numEaten;
        }
    }

    @Override
    public boolean isGameOver() {
        return isTerminal();
    }

    public void display() {
        for (int[] row : board) {
            for (int col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
        System.out.println("History: ");
        System.out.println(history);
        System.out.println();
        System.out.println("Dead");
        System.out.println(numEaten);
    }

    // take int 0-25 return corresponding index into 2d array
    public static Coord oneDToTwoD(int index) {
        int row = index / BOARD_SIZE;
        int col = index % BOARD_SIZE;
        return new Coord(row, col);

    }

    private TigerGameStateRewrite(int[][] board, boolean tigerTurn, Stack<TigerVsDogsMove> history, int numEaten,
            Map<Coord, List<Coord>> adj) {
        this.board = board;
        this.tigerTurn = tigerTurn;
        this.history = history;
        this.numEaten = numEaten;
        this.adj = adj;
    }

    public TigerGameStateRewrite clone() {
        int[][] newBoard = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            newBoard[i] = board[i].clone();
        }

        Stack<TigerVsDogsMove> newHistory = new Stack<>();
        newHistory.addAll(history); // Assuming TigerVsDogsMove is immutable or clone-safe

        return new TigerGameStateRewrite(newBoard, tigerTurn, newHistory, numEaten, adj);
    }

    public static int twoDToOneD(Coord c) {
        return c.row * BOARD_SIZE + c.col;

    }

}
