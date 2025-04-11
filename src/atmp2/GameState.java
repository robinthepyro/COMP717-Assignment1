package atmp2;

import java.util.List;

public interface GameState<M extends Move<M>> {
    /**
     * @return a complete list of valid moves for the current gamestate
     */
    List<M> getValidMoves();

    /**
     * @return An optimized list of valid moves. 
     * If optimization is not required, simply use {@link #getValidMoves()}. 
     * This method is intended for filtering moves based on specific game logic or heuristics 
     * to improve performance (e.g., reducing search space for minimax).
     */
    List<M> getOptimisedValidMoves();

    /**
     * @param move - the move to apply
     * Applies a provided move to the game state
     */
    void applyMove(M move);



    /**
     * @param move The move to undo
     * Reverts the given move on the current game state. 
     */
     void undoMove(M move);

    /**
     * @return true if the game state is terminal (e.g., win, loss, draw), else false
     */
    boolean isTerminal();

    /**
     * @return an integer to compare gamestates for minimax algo
     * The simplest option is to:
     * return -1 on losing terminal states,
     * 1 on winning terminal states,
     * and 0 on non terminal states.
     *
     * However I encourage finding some intermediate heuristic to use in order to separate
     * non terminal states when the minimax depth is too low
     * 
     * Also, try using numbers bigger than +/- 1 on terminal states as this leaves
     * room for intermediate heuristics
     */
    int evaluate();


    
    /**
     * @deprecated This method is currently unused in the minimax algorithm. 
     * The caching of game states was removed, so, this method has been commented out.
     * It might be re-implemented in the future if transposition tables are reintroduced.
     * 
     * @return A unique string representation of the game state.
     */
    // String getMemoKey();

    
    /**
     * @deprecated This method is functionally equivalent to {@link #isTerminal()} and has been
     * deprecated in favor of that method. Please use {@link #isTerminal()} instead.
     * 
     * @return true if the game state is terminal, else false
     */
    @Deprecated
    boolean isGameOver();}

