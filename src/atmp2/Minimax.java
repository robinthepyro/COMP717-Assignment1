package atmp2;

import java.util.Random;

public class Minimax<M extends Move<M>, S extends GameState<M>> {
    private int maxDepth;
    private int nodesEvaluated;
    private final int mode;
    private boolean ab;
    private boolean limited;

    public static final int AB_LIMITED = 0;
    public static final int ABCOMPLETE = 1;
    public static final int MINIMAXLIMITED = 2;
    public static final int MINIMAX_COMPLETE = 3;
    public static final int RANDOM = 4;

    public Minimax(int maxDepth, int mode) {
        this.maxDepth = maxDepth;
        this.mode = mode;
        init();
    }

    public Minimax(int mode){
        // sanity check my code
        if (mode > RANDOM | mode < AB_LIMITED){
            System.err.println("You Should Not Be Doing that. Do better <3");
            System.out.println("Listen to this song and think about what you have done");
            throw new IllegalArgumentException("https://open.spotify.com/track/3gq0rDxpS9e6sbO72PBWbn");
        }
        this.mode = mode;
        init();
    }

    private void init(){
        if (mode == MINIMAXLIMITED | mode == AB_LIMITED){
            limited = true;
        }
        else{
            limited = false;
        }
        if (mode == AB_LIMITED | mode == ABCOMPLETE){
            ab=true;
        }
        else {
            ab=false;
        }
      
    }


    public M getRandomMove(S state) {
        if (!state.isTerminal()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(state.getValidMoves().size());
            return state.getValidMoves().get(randomIndex);
        }
        return null;
    }

    public M getFirstMove(S state) {
        return state.getValidMoves().get(0);
    }

    public M getBestMove(S state, boolean maximizing) {
        // stupid way to make minimax play random moves
        // yeah, we have stupid mode
        // yeah, it makes the ai stupid
        if (mode == RANDOM){
            return getRandomMove(state);
        }
        // GameState<M> clonedState = state.clone();
        nodesEvaluated = 0;
        M bestMove = null;
        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (M move : state.getOptimisedValidMoves()) {
            state.applyMove(move);
            int score;
            score = minimax(state, 1, !maximizing, Integer.MAX_VALUE, Integer.MIN_VALUE);
            state.undoMove(move);

            if ((maximizing && score > bestScore) || (!maximizing && score < bestScore) || bestMove == null) {
                bestScore = score;
                // bestMove = move.clone();
                bestMove = move;
            }
        }

        System.out.println("Nodes evaluated: " + nodesEvaluated);
        return bestMove;
    }

    private int minimax(GameState<M> state, int depth, boolean maximizing, int alpha, int beta) {

        nodesEvaluated++;
        if (limited) {
            if (state.isTerminal() || depth >= maxDepth) {
                return state.evaluate();
            }
        }
        else {
            if (state.isTerminal()){
                return state.evaluate();
            }
        }
        int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (M move : state.getOptimisedValidMoves()) {
            state.applyMove(move);
            int score = minimax(state, depth + 1, !maximizing, alpha, beta);
            state.undoMove(move);

            if (maximizing) {
                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
            } else {
                best = Math.min(best, score);
                beta = Math.min(beta, best);
            }

            if (ab) {
                if (beta <= alpha)
                    break;

            }
        }

        return best;
    }
}
