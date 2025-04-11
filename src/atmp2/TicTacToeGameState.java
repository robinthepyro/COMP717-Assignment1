package atmp2;

import java.util.*;

public class TicTacToeGameState implements GameState<TicTacToeMove> {
    private int[][] board; // 0 for empty, 1 for X, 2 for O
    private int currentPlayer; // 1 for X, 2 for O
    private int movesPlayed;

    public TicTacToeGameState() {
        board = new int[3][3];
        currentPlayer = 1; // X starts
        movesPlayed = 0;
    }

    private TicTacToeGameState(int[][] board, int currentPlayer, int movesPlayed) {
        this.board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, 3);
        }
        this.currentPlayer = currentPlayer;
        this.movesPlayed = movesPlayed;
    }

    @Override
    public List<TicTacToeMove> getValidMoves() {
        List<TicTacToeMove> moves = new ArrayList<>();
        if (isGameOver()) return moves;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    moves.add(new TicTacToeMove(i, j, currentPlayer));
                }
            }
        }
        return moves;
    }

    @Override
    public List<TicTacToeMove> getOptimisedValidMoves() {
        // For TicTacToe, there's no special optimization needed
        return getValidMoves();
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
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    @Override
    public boolean isGameOver() {
        // Check for winner
        if (getWinner() != 0) return true;
        
        // Check for draw (all cells filled)
        return movesPlayed >= 9;
    }

    @Override
    public boolean isTerminal() {
        return isGameOver();
    }

    public int getWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != 0 && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return board[0][j];
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
    public int getScore() {
        int winner = getWinner();
        if (winner == 1) return 1;  // X wins
        if (winner == 2) return -1; // O wins
        if (isGameOver()) return 0; // Draw
        return 0; // Game not over
    }

    @Override
    public int evaluate() {
        int winner = getWinner();
        if (winner == 1) return 1;      // X wins
        if (winner == 2) return -1;     // O wins
        if (isGameOver()) return 0;     // Draw
        
        // Simple heuristic: count potential winning lines
        int xPotential = countPotentialWins(1);
        int oPotential = countPotentialWins(2);
        
        return xPotential - oPotential;
    }

    private int countPotentialWins(int player) {
        int count = 0;
        
        // Check rows
        for (int i = 0; i < 3; i++) {
            int playerCount = 0;
            int emptyCount = 0;
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == player) playerCount++;
                if (board[i][j] == 0) emptyCount++;
            }
            if (playerCount > 0 && emptyCount > 0 && playerCount + emptyCount == 3) count++;
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            int playerCount = 0;
            int emptyCount = 0;
            for (int i = 0; i < 3; i++) {
                if (board[i][j] == player) playerCount++;
                if (board[i][j] == 0) emptyCount++;
            }
            if (playerCount > 0 && emptyCount > 0 && playerCount + emptyCount == 3) count++;
        }
        
        // Check diagonal
        int playerCount = 0;
        int emptyCount = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][i] == player) playerCount++;
            if (board[i][i] == 0) emptyCount++;
        }
        if (playerCount > 0 && emptyCount > 0 && playerCount + emptyCount == 3) count++;
        
        // Check other diagonal
        playerCount = 0;
        emptyCount = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][2-i] == player) playerCount++;
            if (board[i][2-i] == 0) emptyCount++;
        }
        if (playerCount > 0 && emptyCount > 0 && playerCount + emptyCount == 3) count++;
        
        return count;
    }

    @Override
    public GameState<TicTacToeMove> clone() {
        return new TicTacToeGameState(board, currentPlayer, movesPlayed);
    }

    @Override
    public String getMemoKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]);
            }
        }
        sb.append(currentPlayer);
        return sb.toString();
    }

    public void displayGameState() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                char symbol = ' ';
                if (board[i][j] == 1) symbol = 'X';
                if (board[i][j] == 2) symbol = 'O';
                System.out.print(symbol + " | ");
            }
            System.out.println("\n---------");
        }
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
