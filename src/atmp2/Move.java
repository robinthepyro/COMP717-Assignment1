package atmp2;

/**
 * This interface represents a move in a game. The type parameter {@code M}
 * ensures that each
 * move implementation is type-safe and specific to the game it belongs to. For
 * example, a
 * {@code TicTacToeMove} can only be used with {@code TicTacToeGameState} and
 * not with another
 * game's state.
 *
 * The reason we use a self referential generic (@code M extends Move<M>) is
 * toensure that each
 * move can clone itself and return a move of the correct type.
 * Basically, a NimMove when cloned will return a NimMove, not just a Move
 *
 * We are also effectively baking in what type of game matches what move, which
 * prevents us from
 * doing stupid shit like passing Nim moves to TicTacToe games. This isn't a
 * huge deal as this
 * would be a pretty loud bug, but w/e.
 * 
 * @param <M> The type of move. E.g {@link atmp2.NimMove}
 *            {@link atmp2.TicTacToeMove}
 */

public interface Move<M extends Move<M>> {
    /**
     * @return Move<M> that is a deep copy of current move
     */
    M clone();

    // TODO That TODO down there might be really dumb. Do we even need this
    // method???
    // TODO make this more generic, it could really be any type that extends
    // Comparable
    /**
     * @return a value that extends {@link Comparable} and allows minimax to look at
     *         moves
     *         in order.
     */
    Integer sortBy();
}
