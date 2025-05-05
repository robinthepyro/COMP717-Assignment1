package atmp2;

enum Side {
    LEFT,
    RIGHT
}

public class CoinGameMove implements Move<CoinGameMove> {
    public final Side side;
    public final int player;

    public CoinGameMove(Side side, int player) {
        this.side = side;
        this.player = player;
    }

    @Override
    public CoinGameMove clone() {
        return new CoinGameMove(side, player);
    }

    @Override
    public Integer sortBy() {
        return 0;
    }

    @Override
    public String toString() {
        return side.toString();
    }
}
