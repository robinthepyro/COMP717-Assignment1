package atmp2;

import java.util.Scanner;

public class TicTacToeGame {
    private static final int X_PLAYER = 1;
    private static final int O_PLAYER = 2;

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
            boolean isPlayerTurn = (currentPlayer == X_PLAYER && playerIsX) || (currentPlayer == O_PLAYER && !playerIsX);

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
            boolean playerWon = (winner == X_PLAYER && playerIsX) || (winner == O_PLAYER && !playerIsX);
            System.out.println("Game over! " + (playerWon ? "You win!" : "AI wins!"));
        }
    }

    private void playerMove() {
        System.out.println("Your turn (" + (gameState.getCurrentPlayer() == X_PLAYER ? "X" : "O") + ")");
        
        int row, col;
        boolean validMove = false;

        do {
            // Input validation for row and column
            row = getValidInput("Enter row (0-2): ");
            col = getValidInput("Enter column (0-2): ");
            
            try {
                TicTacToeMove move = new TicTacToeMove(row, col, gameState.getCurrentPlayer());
                if (gameState.getValidMoves().contains(move)) {
                    gameState.applyMove(move);
                    validMove = true;
                    System.out.println("You played: " + move);
                } else {
                    System.out.println("Invalid move. This spot is already taken or out of bounds. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        } while (!validMove);
    }

    private int getValidInput(String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input >= 0 && input <= 2) {
                    break;
                } else {
                    System.out.println("Input out of bounds! Please enter a value between 0 and 2.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        }
        return input;
    }

    private void aiMove() {
        System.out.println("AI is thinking...");
        boolean aiIsMaximizing = gameState.getCurrentPlayer() == X_PLAYER; // X maximizes

        TicTacToeMove bestMove = minimax.getBestMove(gameState, aiIsMaximizing);

        System.out.println("AI plays: " + bestMove);
        gameState.applyMove(bestMove);
    }

    // New method to get valid player input (X or O)
    private static boolean getValidPlayerSelection(Scanner scanner) {
        String input;
        while (true) {
            System.out.print("Do you want to play as X (goes first) or O (goes second)? Enter X or O: ");
            input = scanner.nextLine().toUpperCase();

            if (input.equals("X")) {
                return true; // Player is X
            } else if (input.equals("O")) {
                return false; // Player is O
            } else {
                System.out.println("Invalid input! Please enter X or O.");
            }
        }
    }

    // New method to get valid difficulty level (between 1 and 10)
    private static int getValidDifficulty(Scanner scanner) {
        int difficulty;
        while (true) {
            System.out.print("Enter AI difficulty (1-10): ");
            if (scanner.hasNextInt()) {
                difficulty = scanner.nextInt();
                if (difficulty >= 1 && difficulty <= 10) {
                    break;
                } else {
                    System.out.println("Invalid difficulty! Please enter a number between 1 and 10.");
                }
            } else {
                System.out.println("Invalid input! Please enter a valid integer.");
                scanner.next(); // Clear invalid input
            }
        }
        return difficulty;
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);

        // Get valid player selection and difficulty from the user
        boolean playerIsX = getValidPlayerSelection(scanner);
        int difficulty = getValidDifficulty(scanner);
        int maxDepth = Math.max(1, Math.min(10, difficulty));

        // Initialize the game with the valid player selection and difficulty
        TicTacToeGame game = new TicTacToeGame(playerIsX, maxDepth);
        game.play();

        scanner.close();
    }
}
