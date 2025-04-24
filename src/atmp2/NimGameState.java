package atmp2;

import java.util.*;

public class NimGameState implements GameState<NimMove> {
    private int[] piles;
    private int player;

    // these two constants really are an ugly way of doing this
    // ideally this gamestate shouldn't care who is making what moves
    // and should be portable to human vs human games
    // TODO refactor this shitty code
    private static int AI_PLAYER = 1;
    private static int HUMAN_PLAYER = 2;
    private static int[] defaultGameState = {1,3,5,7,5,3,1};
    // private static int[] defaultGameState = {10,10,10,10,10,10};

    // yes yes, the multiple constructors are goofy, no I don't feel like changing it
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

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

    public void switchPlayer(){
        player = (player == AI_PLAYER) ? HUMAN_PLAYER : AI_PLAYER;
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
            if (pileSize == 0) continue;

            // Always add small strategic moves (1 and 2)
            if (pileSize >= 1) moves.add(new NimMove(i, 1));
            if (pileSize >= 2) moves.add(new NimMove(i, 2));
            if (pileSize >= 3) moves.add(new NimMove(i, 3));

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
    public boolean isTerminal(){
        for (int pips: piles){
            if (pips > 0) {
                return false;
            }
        }
        return true;
    }
    
    // TODO remove this redundant method once the interface no longer includes it
    // we are waiting on removal from TicTacToeGameState first but that's a mess rn
    public boolean isGameOver() {
        return isTerminal();
    }

    @Override
    public int evaluate() {
        if (isTerminal()){
            return (player == 1)? 1 : -1;
        }
        return 0;
    }


    // TODO make this pretty again
    // I want the piles printed vertically and with nice spacing and annotations
    public void displayGameState() {
        System.out.println("Current Game State:");
        for (int i = 0; i < piles.length; i++) {
            System.out.printf("Pile %d: %s (%d)\n", i, "● ".repeat(piles[i]), piles[i]);
        }
    }
}
