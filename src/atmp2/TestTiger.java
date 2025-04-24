package atmp2;

import atmp2.TigerVsDogsGame;
import atmp2.TigerVsDogsGameState;
import atmp2.TigerVsDogsMove;

public class TestTiger {
    public static void main(String[] args) {
        TigerVsDogsGameState g = new TigerVsDogsGameState();
        TigerVsDogsMove move1 = new TigerVsDogsMove(12, 6);
        TigerVsDogsMove move2 = new TigerVsDogsMove(4, 8);

        TigerVsDogsMove move3 = new TigerVsDogsMove(6, 18);
        g.applyMove(move1);
        g.applyMove(move2);
        g.applyMove(move3);

        for (TigerVsDogsMove m : g.getValidMoves()){
            System.out.println(m.toString());
        }

        TigerVsDogsGame.printBoard(g);
        g.undoMove(move3);
        System.out.print(move1);
        System.out.println();
        TigerVsDogsGame.printBoard(g);


    }
    
}
