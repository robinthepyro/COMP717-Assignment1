package atmp2;

import java.util.List;

public interface GameState<M extends Move<M>> {
    List<M> getValidMoves();
    List<M> getOptimisedValidMoves();
    void applyMove(M move);
    void undoMove(M move);
    boolean isGameOver();
    boolean isTerminal();
    int getScore();
    int evaluate();
    GameState<M> clone();
    String getMemoKey();
}

