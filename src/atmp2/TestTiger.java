package atmp2;

import java.util.Random;

import atmp2.Minimax;
import atmp2.TigerVsDogsGame;
import atmp2.TigerVsDogsGameState;
import atmp2.TigerVsDogsMove;

public class TestTiger {
    public static void main(String[] args) {
        TigerVsDogsGame game = new TigerVsDogsGame();

        // test get validmoves
        
        // test apply / undo
        TigerVsDogsMove move1 = new TigerVsDogsMove(12, 11);
        TigerVsDogsMove move2 = new TigerVsDogsMove(0, 6);
        TigerVsDogsMove move3 = new TigerVsDogsMove(1, 7);
        TigerVsDogsMove move4 = new TigerVsDogsMove(11, 12);
        TigerVsDogsMove move5 = new TigerVsDogsMove(6, 11);
        TigerVsDogsMove move6 = new TigerVsDogsMove(12, 6);
        game.state.applyMove(move1);
        game.displayGame(game.state);
        game.state.applyMove(move2);
        game.displayGame(game.state);
        game.state.applyMove(move3);
        game.displayGame(game.state);
        game.state.applyMove(move4);
        game.displayGame(game.state);
        game.state.applyMove(move5);
        game.displayGame(game.state);
        game.state.applyMove(move6);
        game.displayGame(game.state);
        // test evaluate
        // System.out.println(game.state.evaluate());
        // test isTerminal

    }

}
