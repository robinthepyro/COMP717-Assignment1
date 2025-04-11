package atmp2;

public class TicTacToeMove implements Move<TicTacToeMove> {
    public final int row;
    public final int col;
    public final int player; // 1 for X, 2 for O

    public TicTacToeMove(int row, int col, int player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }

    @Override
    public TicTacToeMove clone() {
        return new TicTacToeMove(row, col, player);
    }

    @Override
    public String toString() {
        return "Player " + (player == 1 ? "X" : "O") + " places at (" + row + "," + col + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TicTacToeMove other)) return false;
        return row == other.row && col == other.col && player == other.player;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * row + col) + player;
    }

    @Override
    public Integer sortBy() {
        // Sort by row and column position
        return row * 3 + col;
    }
}
