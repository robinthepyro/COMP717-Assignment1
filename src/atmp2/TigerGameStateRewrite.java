package atmp2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TigerGameStateRewrite implements GameState<TigerVsDogsMove> {
    public int numEaten;
    private Stack<TigerVsDogsMove> history;
    private static final int BOARD_SIZE = 5;
    public int[][] board;
    Map<Coord, List<Coord>> adj;
    public boolean tigerTurn;
    public static int TIGER = 9;
    public static int DOG = 1;
    public static int EMPTY = 0;
    private static List<Integer> EATRULE = Arrays.asList(EMPTY, DOG, TIGER, DOG, EMPTY);

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
                { DOG, DOG, EMPTY, DOG, DOG },
                { DOG, EMPTY, DOG, EMPTY, DOG },
                { DOG, EMPTY, TIGER, EMPTY, DOG },
                { DOG, DOG, DOG, EMPTY, DOG },
                { DOG, DOG, EMPTY, DOG, DOG } };
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
                    int start = Collections.indexOfSubList(extended, EATRULE);
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
                    int start = Collections.indexOfSubList(extended, EATRULE);
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
        List<TigerVsDogsMove> ret = new ArrayList<>();
        if (tigerTurn) {
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

    @Override
    public void applyMove(TigerVsDogsMove move) {

        int temp = getNode(move.startNode);
        setNode(move.startNode, 0);
        setNode(move.endNode, temp);
        if (canEat()) {
            move.deadDogs = getEaten();
        }

        // add to stack
        history.push(move);

        // Switch turns
        tigerTurn = !tigerTurn;
    }

    // i can already feel the incoming jank
    // we are gonna take in a move param and ignore it.
    @Override
    public void undoMove(TigerVsDogsMove move) {
        setNode(move.startNode, getNode(move.endNode));
        setNode(move.endNode, EMPTY);

        if (move.deadDogs != null) {
            for (Coord c : move.deadDogs) {
                setNode(c, DOG);
            }
        }

        history.pop();
        tigerTurn = !tigerTurn;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int evaluate() {
        return numEaten;
    }

    @Override
    public boolean isGameOver() {
        return isTerminal();
    }

    // take int 0-25 return corresponding index into 2d array
    private Coord oneDToTwoD(int index) {
        int row = index / BOARD_SIZE;
        int col = index % BOARD_SIZE;
        return new Coord(row, col);

    }

    private int twoDToOneD(Coord c) {
        return c.row * BOARD_SIZE + c.col;

    }

}
