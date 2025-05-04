package atmp2;

import java.util.*;

public class NimGameState implements GameState<NimMove> {
    public static final int AI_PLAYER = 1;
    public static final int HUMAN_PLAYER = 2;

    private static final int[] defaultGameState = { 1, 3, 5, 7, 5, 3, 1 };
    // private static int[] defaultGameState = {10,10,10,10,10,10};

    private int[] piles;
    private int player;

    // yes yes, the multiple constructors are goofy, no I don't feel like changing
    // it
    public NimGameState(int[] initialPiles, int player) {
        this.piles = Arrays.copyOf(initialPiles, initialPiles.length);
        this.player = player;
    }

    public NimGameState(int[] initialPiles) {
        this(initialPiles, AI_PLAYER);
    }

    public NimGameState() {
        this(defaultGameState, AI_PLAYER);
    }

    public NimGameState(boolean humanStarts) {
        this(defaultGameState, humanStarts ? HUMAN_PLAYER : AI_PLAYER);
    }

    public int[] getPiles() {
        return piles;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void switchPlayer() {
        player = (player == AI_PLAYER) ? HUMAN_PLAYER : AI_PLAYER;
    }

    public NimGameState clone() {
        return new NimGameState(piles, player);
    }

    @Override
    public List<NimMove> getValidMoves() {
        List<NimMove> moves = new ArrayList<>();
        for (int i = 0; i < piles.length; i++) {
            for (int j = 1; j <= piles[i]; j++) {
                moves.add(new NimMove(i, j));
            }
        }
        return moves;
    }

    // TODO clean this method up, make it legible
    // Maybe look at optimizing the amount of iterations it makes
    // We really want this to run fast
    @Override
    public List<NimMove> getOptimisedValidMoves() {
        List<NimMove> moves = new ArrayList<>();
        for (int i = 0; i < piles.length; i++) {
            int pileSize = piles[i];
            if (pileSize == 0)
                continue;

            // Always add small strategic moves (1 and 2)
            if (pileSize >= 1)
                moves.add(new NimMove(i, 1));
            if (pileSize >= 2)
                moves.add(new NimMove(i, 2));
            if (pileSize >= 3)
                moves.add(new NimMove(i, 3));

            // Add max and max-1 (if meaningful)
            if (pileSize > 3) {
                moves.add(new NimMove(i, pileSize));
                if (pileSize - 1 > 3) {
                    moves.add(new NimMove(i, pileSize - 1));
                }
            }
        }

        // Sort using the sortBy method from Move interface (descending)
        moves.sort(Comparator.comparingInt(NimMove::sortBy).reversed());

        return moves;
    }

    @Override
    public void applyMove(NimMove move) {
        piles[move.pileIndex] -= move.pips;
        switchPlayer();
    }

    @Override
    public void undoMove(NimMove move) {
        piles[move.pileIndex] += move.pips;
        switchPlayer();
    }

    @Override
    public boolean isTerminal() {
        for (int pips : piles) {
            if (pips > 0) {
                return false;
            }
        }
        return true;
    }

    // simplest evaluation of gamestate is to check who won
    // We don't really need to bother with Nim Sum calculations
    // because this just works.
     @Override
     public int evaluate() {
         if (isTerminal()) {
             return (player == 1) ? 1 : -1;
         }
         return 0;
     }
    
}
