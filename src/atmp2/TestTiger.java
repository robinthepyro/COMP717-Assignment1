package atmp2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import atmp2.TigerVsDogsGame;
import atmp2.TigerVsDogsGameState;
import atmp2.TigerVsDogsMove;

public class TestTiger {
    public static void main(String[] args) {
        // Initialize the game state
        TigerVsDogsGameState gameState = new TigerVsDogsGameState();
        
        // Print initial game state
        System.out.println("Initial Game State:");
        TigerVsDogsGame.displayGame(gameState);
        
        // Create moves for the Tiger to capture multiple dogs
        TigerVsDogsMove move1 = new TigerVsDogsMove(12, 7); // Tiger moves from position 12 to 7
        TigerVsDogsMove move2 = new TigerVsDogsMove(7, 2);  // Tiger moves from position 7 to 2 (captures dog)
        TigerVsDogsMove move3 = new TigerVsDogsMove(2, 6);  // Tiger moves from position 2 to 6
        TigerVsDogsMove move4 = new TigerVsDogsMove(6, 10); // Tiger moves from position 6 to 10 (captures dog)
        
        // Apply the moves and capture dogs
        System.out.println("\nApplying moves and capturing dogs:");
        gameState.applyMove(move1);
        System.out.println("After move 1:");
        TigerVsDogsGame.displayGame(gameState);
        
        gameState.applyMove(move2);
        System.out.println("After move 2:");
        TigerVsDogsGame.displayGame(gameState);
        
        gameState.applyMove(move3);
        System.out.println("After move 3:");
        TigerVsDogsGame.displayGame(gameState);
        
        gameState.applyMove(move4);
        System.out.println("After move 4 (capturing two dogs):");
        TigerVsDogsGame.displayGame(gameState);
        
        // Now undo the last two moves to restore to a state after move 2
        System.out.println("\nUndoing the last two moves (move 4 and move 3):");
        gameState.undoMove(move4); // Undo move 4
        System.out.println("After undoing move 4:");
        TigerVsDogsGame.displayGame(gameState);
        
        gameState.undoMove(move3); // Undo move 3
        System.out.println("After undoing move 3:");
        TigerVsDogsGame.displayGame(gameState);
        
        // Now check the state, we should have the following:
        // Tiger at position 2, one dead dog (from move 2)
        // All other dogs should still be on the board
        System.out.println("\nState after undoing move 3 and 4 (checking intermediate state):");
        TigerVsDogsGame.displayGame(gameState);
        
        // Verify that the state matches the expected state after undoing moves
        Integer tigerPos = gameState.getTigerPosition();
        System.out.println("\nTiger's position after undoing: " + tigerPos);
        System.out.println("Dead dogs count after undoing: " + gameState.getDeadDogs());
        
        // Let's apply a few more moves to make sure the state is still correct
        TigerVsDogsMove move5 = new TigerVsDogsMove(2, 8);  // Tiger moves from 2 to 8
        gameState.applyMove(move5);
        System.out.println("\nAfter applying move 5 (Tiger moves to 8):");
        TigerVsDogsGame.displayGame(gameState);
        
        // Apply a final move
        TigerVsDogsMove move6 = new TigerVsDogsMove(8, 13); // Tiger moves from 8 to 13 (captures another dog)
        gameState.applyMove(move6);
        System.out.println("\nAfter applying move 6 (Tiger moves to 13, captures another dog):");
        TigerVsDogsGame.displayGame(gameState);
        
        // Final check of the game state
        System.out.println("\nFinal Game State:");
        TigerVsDogsGame.displayGame(gameState);
        
        // Verify the number of dead dogs and the Tiger's final position
        System.out.println("\nFinal check:");
        System.out.println("Tiger's final position: " + gameState.getTigerPosition());
        System.out.println("Number of dead dogs: " + gameState.getDeadDogs());
    }

}


