package atmp2;

import java.util.Random;

public class Minimax<M extends Move<M>, S extends GameState<M>> {
    private int maxDepth;
    private int nodesEvaluated;

    public Minimax(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public M getRandomMove(S state) {
        if (!state.isTerminal()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(state.getValidMoves().size()); // Generates a random index
            return state.getValidMoves().get(randomIndex);
        }
        return null;
    }

    public M getFirstMove(S state) {
        return state.getValidMoves().get(0);
    }

    public M getBestMove(S state, boolean maximizing) {
        nodesEvaluated = 0;
        M bestMove = null;
        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (M move : state.getOptimisedValidMoves()) {
            state.applyMove(move);
            int score = minimax(state, 1, !maximizing, Integer.MIN_VALUE, Integer.MAX_VALUE);
            state.undoMove(move);

            if ((maximizing && score > bestScore) || (!maximizing && score < bestScore) || bestMove == null) {
                bestScore = score;
                bestMove = move.clone();
            }
        }

        System.out.println("Nodes evaluated: " + nodesEvaluated);
        return bestMove;
    }

    private int minimax(S state, int depth, boolean maximizing, int alpha, int beta) {
        nodesEvaluated++;

        if (state.isTerminal() || depth >= maxDepth) {
            return state.evaluate();
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

            if (beta <= alpha) break;
        }

        return best;
    }
}
