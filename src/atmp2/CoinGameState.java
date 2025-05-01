package atmp2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoinGameState implements GameState<CoinGameMove> {
    public static final int PLAYER_HUMAN = 0;
    public static final int PLAYER_AI = 1;
    private int gameSize;

    private int[] board; // Array of coin values
    private int leftPointer; // Tracks where the next number can be taken from
    private int rightPointer;
    private int currentPlayer;

    private int playerScore = 0;
    private int aiScore = 0;

    public CoinGameState(int startingPlayer, int gameSize) {
        this.gameSize = gameSize;
        currentPlayer = startingPlayer;

        leftPointer = 0;
        rightPointer = gameSize - 1;

        fillBoard();
    }

    private CoinGameState(int[] board, int gameSize, int currentPlayer, int leftPointer, int rightPointer,
            int playerScore, int aiScore) {
        this.board = board;
        this.gameSize = gameSize;
        this.currentPlayer = currentPlayer;
        this.leftPointer = leftPointer;
        this.rightPointer = rightPointer;
        this.playerScore = playerScore;
        this.playerScore = aiScore;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getAiScore() {
        return aiScore;
    }

    /**
     * Switches to the AI player if the human is currently playing. Visa Versa.
     */
    public void swapPlayer() {
        currentPlayer = (currentPlayer == CoinGameState.PLAYER_HUMAN) ? CoinGameState.PLAYER_AI
                : CoinGameState.PLAYER_HUMAN;
    }

    private void fillBoard() {
        board = new int[gameSize];
        Random random = new Random();
        for (int i = 0; i < gameSize; i++) {
            board[i] = random.nextInt(9) + 1;
        }
    }

    public int getWinner() {
        return (playerScore > aiScore) ? PLAYER_HUMAN : PLAYER_AI;
    }

    @Override
    public List<CoinGameMove> getValidMoves() {
        List<CoinGameMove> moves = new ArrayList<>();

        if (isTerminal())
            return moves;

        moves.add(new CoinGameMove(Side.LEFT, currentPlayer));
        moves.add(new CoinGameMove(Side.RIGHT, currentPlayer));

        return moves;
    }

    @Override
    public List<CoinGameMove> getOptimisedValidMoves() {
        return getValidMoves();
    }

    @Override
    public void applyMove(CoinGameMove move) {
        int addedScore = 0;
        if (move.side == Side.LEFT) {
            addedScore = board[leftPointer++];
        } else {
            addedScore = board[rightPointer--];
        }

        if (currentPlayer == CoinGameState.PLAYER_HUMAN) {
            playerScore += addedScore;
        } else {
            aiScore += addedScore;
        }

        swapPlayer();
    }

    @Override
    public void undoMove(CoinGameMove move) {
        int removedScore = 0;
        if (move.side == Side.LEFT) {
            removedScore = board[--leftPointer];
        } else {
            removedScore = board[++rightPointer];
        }

        if (move.player == CoinGameState.PLAYER_HUMAN) {
            playerScore -= removedScore;
        } else {
            aiScore -= removedScore;
        }
        swapPlayer();
    }

    @Override
    public boolean isTerminal() {
        return leftPointer >= rightPointer;
    }

    @Override
    public int evaluate() {
        return aiScore - playerScore;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < board.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            if (i == leftPointer && i == rightPointer) {
                sb.append(">").append(board[i]).append("<");
            } else if (i == leftPointer) {
                sb.append(">").append(board[i]);
            } else if (i == rightPointer) {
                sb.append(board[i]).append("<");
            } else {
                sb.append(board[i]);
            }
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public CoinGameState clone() {
        return new CoinGameState(board, gameSize, currentPlayer, leftPointer, rightPointer, playerScore, aiScore);
    }
}
