package atmp2;

import java.util.Random;

public class TestTiger {
    public static void main(String[] args) {
        TigerVsDogsGame game = new TigerVsDogsGame();
        // Minimax<TigerVsDogsMove, TigerVsDogsGameState> minimax = new Minimax<>(1);

        // int[] b = {
        //         0, 0, 1, 0, 0,
        //         0, 0, 0, 0, 0,
        //         0, 0, 0, -3, 1,
        //         0, 0, 0, 0, 0,
        //         0, 0, 0, 0, 0,
        // };
        // game.state.board = b;
        // game.displayGame(game.state);
        // System.out.println(game.state.getDogsToEat(3));
        // game.state.applyMove(new TigerVsDogsMove(13, 8));

        // game.displayGame(game.state);
        while (true){
            game = new TigerVsDogsGame();
            game.run();
            
        }
        // game.run();

    }

}
