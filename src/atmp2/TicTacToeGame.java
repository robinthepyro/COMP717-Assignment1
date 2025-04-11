package atmp2;

import java.util.Scanner;

public class TicTacToeGame {
    private TicTacToeGameState gameState;
    private Minimax<TicTacToeMove, TicTacToeGameState> minimax;
    private Scanner scanner;
    private boolean playerIsX;
    
    public TicTacToeGame(boolean playerIsX, int maxDepth) {
        this.gameState = new TicTacToeGameState();
        this.minimax = new Minimax<>(maxDepth);
        this.scanner = new Scanner(System.in);
        this.playerIsX = playerIsX;
    }
    
    public void play() {
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println("You are " + (playerIsX ? "X" : "O") + " and " + 
                          (playerIsX ? "go first" : "go second"));
        
        gameState.displayGameState();
        
        while (!gameState.isGameOver()) {
            int currentPlayer = gameState.getCurrentPlayer();
            boolean isPlayerTurn = (currentPlayer == 1 && playerIsX) || (currentPlayer == 2 && !playerIsX);
            
            if (isPlayerTurn) {
                playerMove();
            } else {
                aiMove();
            }
            
            gameState.displayGameState();
        }
        
        // Game over
        int winner = gameState.getWinner();
        if (winner == 0) {
            System.out.println("Game over! It's a draw!");
        } else {
            boolean playerWon = (winner == 1 && playerIsX) || (winner == 2 && !playerIsX);
            System.out.println("Game over! " + (playerWon ? "You win!" : "AI wins!"));
        }
    }
    
    private void playerMove() {
        System.out.println("Your turn (" + (gameState.getCurrentPlayer() == 1 ? "X" : "O") + ")");
        
        int row, col;
        boolean validMove = false;
        
        do {
            System.out.print("Enter row (0-2): ");
            row = scanner.nextInt();
            System.out.print("Enter column (0-2): ");
            col = scanner.nextInt();
            
            // Check if the move is valid
            try {
                TicTacToeMove move = new TicTacToeMove(row, col, gameState.getCurrentPlayer());
                if (gameState.getValidMoves().contains(move)) {
                    gameState.applyMove(move);
                    validMove = true;
                    System.out.println("You played: " + move);
                } else {
                    System.out.println("Invalid move. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        } while (!validMove);
    }
    
    private void aiMove() {
        System.out.println("AI is thinking...");
        // Determine if AI should maximize or minimize based on which player it is
        boolean aiIsMaximizing = gameState.getCurrentPlayer() == 1; // X maximizes
    
        TicTacToeMove bestMove = minimax.getBestMove(gameState, aiIsMaximizing);
    
        System.out.println("AI plays: " + bestMove);
        gameState.applyMove(bestMove);
    }

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Do you want to play as X (goes first) or O (goes second)?");
        System.out.print("Enter X or O: ");
        String input = scanner.nextLine().toUpperCase();
        boolean playerIsX = input.equals("X");
        
        System.out.println("Enter AI difficulty (1-10): ");
        int difficulty = scanner.nextInt();
        int maxDepth = Math.max(1, Math.min(10, difficulty));
        
        TicTacToeGame game = new TicTacToeGame(playerIsX, maxDepth);
        game.play();
        
        scanner.close();
    }
}
