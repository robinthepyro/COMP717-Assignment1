package atmp2;


/*
So, I can explain the cursed interface here

The reason we have Move<M extends Move<M>> is to allow this
interface to be type safe.

We wouldn't want to be passing TicTacToeMove's to NimGameState and this is how we achieve this.

Basically, if a class NimMove is implementing Move<NimMove> we can clone it and know that it
will stay a NimMove



*/
public interface Move<M extends Move<M>> {
    M clone();

    Integer sortBy();
}

