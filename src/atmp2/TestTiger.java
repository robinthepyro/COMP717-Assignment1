package atmp2;

import java.util.Random;

import atmp2.Minimax;
import atmp2.TigerVsDogsGame;
import atmp2.TigerVsDogsGameState;
import atmp2.TigerVsDogsMove;

public class TestTiger {
    public static void main(String[] args) {
        TigerVsDogsGame game = new TigerVsDogsGame();
        Minimax<TigerVsDogsMove, TigerVsDogsGameState> minimax = new Minimax<>(8);

        // test get validmoves
        
        // test apply / undo        game.getDogTurn(game.state);
        game.displayGameWithDogIDs(game.state);

        
         
        while(true){
            game.displayGame(game.state);
            game.state.applyMove(minimax.getBestMove(game.state, true));
            if (game.state.isTerminal()){
                System.out.println("Game Over");
                break;
            }
            game.displayGame(game.state);
            game.state.applyMove(game.getDogTurn(game.state));
            if (game.state.isTerminal()){
                System.out.println("Game Over");
                break;
            }
        }
        System.out.println("The winner is " +game.state.evaluate());

    }

}
