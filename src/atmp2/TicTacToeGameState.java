package atmp2;

// TODO work out what stuff is needed as it definitely shouldn't import all of util
import java.util.*;

public class TicTacToeGameState implements GameState<TicTacToeMove> {
    private static final int X_PLAYER = 1;
    private static final int O_PLAYER = 2;
    private static final int BOARD_SIZE = 3;
    
    private int[][] board;
    private int currentPlayer;
    private int movesPlayed;

    public TicTacToeGameState() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = X_PLAYER; // X starts
        movesPlayed = 0;
    }

    private TicTacToeGameState(int[][] board, int currentPlayer, int movesPlayed) {
        this.board = deepCopyBoard(board);
        this.currentPlayer = currentPlayer;
        this.movesPlayed = movesPlayed;
    }

    public TicTacToeGameState clone(){
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
        if (isGameOver()) return moves;

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
    public boolean isGameOver() {
        return getWinner() != 0 || movesPlayed >= BOARD_SIZE * BOARD_SIZE;
    }

    @Override
    public boolean isTerminal() {
        return isGameOver();
    }

    public int getWinner() {
        // Check rows, columns, and diagonals
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }
        
        // Check diagonals
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }
        
        return 0; // No winner
    }

    @Override
    public int evaluate() {
        int winner = getWinner();
        if (winner == X_PLAYER) return 1;      // X wins
        if (winner == O_PLAYER) return -1;     // O wins
        if (isGameOver()) return 0;            // Draw

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
            count += checkLineForPotentialWin(player, new int[]{board[0][i], board[1][i], board[2][i]});
        }

        // Check diagonals
        count += checkLineForPotentialWin(player, new int[]{board[0][0], board[1][1], board[2][2]});
        count += checkLineForPotentialWin(player, new int[]{board[0][2], board[1][1], board[2][0]});

        return count;
    }

    private int checkLineForPotentialWin(int player, int[] line) {
        int playerCount = 0;
        int emptyCount = 0;
        for (int cell : line) {
            if (cell == player) playerCount++;
            if (cell == 0) emptyCount++;
        }
        return (playerCount > 0 && emptyCount > 0 && playerCount + emptyCount == 3) ? 1 : 0;
    }

    // TODO change this to print the most gorgeous terminal noughts and crosses u have ever seen
    // Try using ASCII box drawing characters
    public void displayGameState() {
        System.out.println("\n-------------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                char symbol = ' ';
                if (board[i][j] == X_PLAYER) symbol = 'X';
                if (board[i][j] == O_PLAYER) symbol = 'O';
                System.out.print(symbol + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
