package atmp2;

import java.util.Scanner;

public class TicTacToeGame implements Game{
    private static final int X_PLAYER = 1;
    private static final int O_PLAYER = 2;

    private final TicTacToeGameState gameState;
    private final Minimax<TicTacToeMove, TicTacToeGameState> minimax;
    private final Scanner scanner;
    private final boolean playerIsX;

    public TicTacToeGame(boolean playerIsX, int maxDepth) {
        this.gameState = new TicTacToeGameState();
        this.minimax = new Minimax<>(maxDepth);
        this.scanner = new Scanner(System.in); // keep internal Scanner
        this.playerIsX = playerIsX;
    }

    public void run() {
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println("You are " + (playerIsX ? "X" : "O") + " and " +
                (playerIsX ? "go first" : "go second"));

        clearScreen();
        gameState.displayGameState();

        while (!gameState.isTerminal()) {
            int currentPlayer = gameState.getCurrentPlayer();
            boolean isPlayerTurn = (currentPlayer == X_PLAYER && playerIsX) ||
                    (currentPlayer == O_PLAYER && !playerIsX);

            if (isPlayerTurn) {
                playerMove();
            } else {
                aiMove();
            }

            clearScreen();
            gameState.displayGameState();
        }

        printResult();
    }

    private void playerMove() {
        System.out.println("Your turn (" + (gameState.getCurrentPlayer() == X_PLAYER ? "X" : "O") + ")");
        int row, col;
        boolean validMove = false;

        do {
            row = getValidInput("Enter row (1-3): ");
            col = getValidInput("Enter column (1-3): ");

            TicTacToeMove move = new TicTacToeMove(row, col, gameState.getCurrentPlayer());

            if (gameState.getValidMoves().contains(move)) {
                gameState.applyMove(move);
                validMove = true;
                System.out.println("You played: " + move);
            } else {
                System.out.println("Invalid move. That spot is taken or out of bounds. Try again.");
            }
        } while (!validMove);
    }

    private int getValidInput(String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                // adjust from human friendly 1 based indexing to 0 based indexing
                input--;
                if (input >= 0 && input <= 2) {
                    scanner.nextLine(); // clear newline
                    return input;
                } else {
                    System.out.println("Input out of bounds! Enter a number between 1 and 3.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // clear invalid input
            }
        }
    }

    private void aiMove() {
        System.out.println("AI is thinking...");
        boolean aiIsMaximizing = gameState.getCurrentPlayer() == X_PLAYER;
        TicTacToeMove bestMove = minimax.getBestMove(gameState, aiIsMaximizing);
        System.out.println("AI plays: " + bestMove);
        gameState.applyMove(bestMove);
    }

    private void printResult() {
        int winner = gameState.getWinner();
        if (winner == 0) {
            System.out.println("Game over! It's a draw!");
        } else {
            boolean playerWon = (winner == X_PLAYER && playerIsX) ||
                    (winner == O_PLAYER && !playerIsX);
            System.out.println("Game over! " + (playerWon ? "You win!" : "AI wins!"));
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
