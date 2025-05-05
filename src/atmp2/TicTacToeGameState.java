package atmp2;

// TODO work out what stuff is needed as it definitely shouldn't import all of util
import java.util.*;

public class TicTacToeGameState implements GameState<TicTacToeMove> {
    private static final int X_PLAYER = 1;
    private static final int O_PLAYER = 2;
    public final int BOARD_SIZE;

    private int[][] board;
    private int currentPlayer;
    private int movesPlayed;

    public TicTacToeGameState() {
        this.BOARD_SIZE = 3;
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = X_PLAYER; // X starts
        movesPlayed = 0;
    }

    public TicTacToeGameState(int boardSize) {
        this.BOARD_SIZE = boardSize;
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = X_PLAYER; // X starts
        movesPlayed = 0;
    }

    private TicTacToeGameState(int[][] board, int currentPlayer, int movesPlayed) {
        this.board = deepCopyBoard(board);
        this.BOARD_SIZE = 3;
        this.currentPlayer = currentPlayer;
        this.movesPlayed = movesPlayed;
    }

    public TicTacToeGameState clone() {
        return new TicTacToeGameState(board, currentPlayer, movesPlayed);
    }

    private int[][] deepCopyBoard(int[][] original) {
        int[][] copy = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, BOARD_SIZE);
        }
        return copy;
    }

    @Override
    public List<TicTacToeMove> getValidMoves() {
        List<TicTacToeMove> moves = new ArrayList<>();
        if (isTerminal())
            return moves;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    moves.add(new TicTacToeMove(i, j, currentPlayer));
                }
            }
        }
        return moves;
    }

    @Override
    public List<TicTacToeMove> getOptimisedValidMoves() {
        return getValidMoves(); // No special optimization needed for TicTacToe
    }

    @Override
    public void applyMove(TicTacToeMove move) {
        if (board[move.row][move.col] != 0) {
            throw new IllegalArgumentException("Invalid move: cell already occupied");
        }
        board[move.row][move.col] = move.player;
        movesPlayed++;
        switchPlayer();
    }

    @Override
    public void undoMove(TicTacToeMove move) {
        board[move.row][move.col] = 0;
        movesPlayed--;
        switchPlayer();
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == X_PLAYER) ? O_PLAYER : X_PLAYER;
    }

    @Override
    public boolean isTerminal() {
        return getWinner() != 0 || movesPlayed >= BOARD_SIZE * BOARD_SIZE;
    }

    public int getWinner() {
        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            int first = board[i][0];
            if (first == 0)
                continue;
            boolean win = true;
            for (int j = 1; j < BOARD_SIZE; j++) {
                if (board[i][j] != first) {
                    win = false;
                    break;
                }
            }
            if (win)
                return first;
        }

        // Check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            int first = board[0][j];
            if (first == 0)
                continue;
            boolean win = true;
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (board[i][j] != first) {
                    win = false;
                    break;
                }
            }
            if (win)
                return first;
        }

        // Check main diagonal
        int first = board[0][0];
        if (first != 0) {
            boolean win = true;
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (board[i][i] != first) {
                    win = false;
                    break;
                }
            }
            if (win)
                return first;
        }

        // Check anti-diagonal
        first = board[0][BOARD_SIZE - 1];
        if (first != 0) {
            boolean win = true;
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (board[i][BOARD_SIZE - 1 - i] != first) {
                    win = false;
                    break;
                }
            }
            if (win)
                return first;
        }

        return 0; // No winner
    }

    @Override
    public int evaluate() {
        int winner = getWinner();
        if (winner == X_PLAYER)
            return 1; // X wins
        if (winner == O_PLAYER)
            return -1; // O wins
        if (isTerminal())
            return 0; // Draw

        // Heuristic: count potential winning lines
        int xPotential = countPotentialWins(X_PLAYER);
        int oPotential = countPotentialWins(O_PLAYER);

        return xPotential - oPotential;
    }

    private int countPotentialWins(int player) {
        int count = 0;

        // Check rows, columns, and diagonals
        for (int i = 0; i < BOARD_SIZE; i++) {
            count += checkLineForPotentialWin(player, board[i]);
            count += checkLineForPotentialWin(player, new int[] { board[0][i], board[1][i], board[2][i] });
        }

        // Check diagonals
        count += checkLineForPotentialWin(player, new int[] { board[0][0], board[1][1], board[2][2] });
        count += checkLineForPotentialWin(player, new int[] { board[0][2], board[1][1], board[2][0] });

        return count;
    }

    private int checkLineForPotentialWin(int player, int[] line) {
        int playerCount = 0;
        int emptyCount = 0;
        for (int cell : line) {
            if (cell == player)
                playerCount++;
            if (cell == 0)
                emptyCount++;
        }
        return (playerCount > 0 && emptyCount > 0 && playerCount + emptyCount == 3) ? 1 : 0;
    }

    public void displayGameState() {
        final String VERT_SEP = "│";
        final String HORIZ = "───";
        final String CROSS = "┼";
        final String TOP_LEFT = "┌", TOP_MID = "┬", TOP_RIGHT = "┐";
        final String MID_LEFT = "├", MID_MID = "┼", MID_RIGHT = "┤";
        final String BOT_LEFT = "└", BOT_MID = "┴", BOT_RIGHT = "┘";

        // Generate top, middle, and bottom borders dynamically
        StringBuilder topBorder = new StringBuilder(TOP_LEFT);
        StringBuilder midSeparator = new StringBuilder(MID_LEFT);
        StringBuilder bottomBorder = new StringBuilder(BOT_LEFT);

        for (int i = 0; i < BOARD_SIZE; i++) {
            topBorder.append(HORIZ);
            midSeparator.append(HORIZ);
            bottomBorder.append(HORIZ);

            if (i < BOARD_SIZE - 1) {
                topBorder.append(TOP_MID);
                midSeparator.append(CROSS);
                bottomBorder.append(BOT_MID);
            } else {
                topBorder.append(TOP_RIGHT);
                midSeparator.append(MID_RIGHT);
                bottomBorder.append(BOT_RIGHT);
            }
        }

        // Print the board
        System.out.println(topBorder);
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(VERT_SEP);
            for (int j = 0; j < BOARD_SIZE; j++) {
                char symbol = ' ';
                if (board[i][j] == X_PLAYER)
                    symbol = 'X';
                if (board[i][j] == O_PLAYER)
                    symbol = 'O';
                System.out.print(" " + symbol + " " + VERT_SEP);
            }
            System.out.println();
            if (i < BOARD_SIZE - 1) {
                System.out.println(midSeparator);
            } else {
                System.out.println(bottomBorder);
            }
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
