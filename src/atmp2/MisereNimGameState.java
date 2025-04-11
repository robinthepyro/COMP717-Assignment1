package atmp2;

import java.util.*;

public class MisereNimGameState implements GameState<NimMove> {
    private int[] piles;
    private int player;

    // these two constants really are an ugly way of doing this
    // ideally this gamestate shouldn't care who is making what moves
    // and should be portable to human vs human games
    //
    // Thats not my problem rn
    private static int AI_PLAYER = 1;
    private static int HUMAN_PLAYER = 2;

    public MisereNimGameState(int[] initialPiles) {
        this.player = 1;
        this.piles = Arrays.copyOf(initialPiles, initialPiles.length);
    }

    public MisereNimGameState() {
        this.player = AI_PLAYER;
        this.piles = new int[] {1,3,5,7,5,3,1};
    }

    public MisereNimGameState(boolean humanStarts){
        this.player = (humanStarts)? HUMAN_PLAYER: AI_PLAYER;
        this.piles = new int[] {1,3,5,7,5,3,1};
    }

    public MisereNimGameState(int[] initialPiles, boolean humanStarts) {
        this.player = (humanStarts)? HUMAN_PLAYER: AI_PLAYER;
        this.piles = Arrays.copyOf(initialPiles, initialPiles.length);
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

    @Override
    public int getScore(){
        return 0;
        
    }

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
    public boolean isGameOver() {
        int total = 0;
        for (int p : piles) total += p;
        return total <= 0;
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
    

    @Override
    public int evaluate() {
        if (isTerminal()){
            return (player == 1)? 1 : -1;
        }
        return 0;
    }

    @Override
    public GameState<NimMove> clone() {
        return new MisereNimGameState(piles);
    }

    @Override
    public String getMemoKey() {
        return Arrays.toString(piles);
    }

    public void displayGameState() {
        // TODO make this pretty again
        // I want the piles printed vertically and with nice spacing and annotations
        for (int i = 0; i < piles.length; i++) {
            System.out.print("Pile " + i + ": ");
            for (int j = 0; j < piles[i]; j++) {
                System.out.print("● ");
            }
            System.out.println("(" + piles[i] + ")");
        }
    }
}
