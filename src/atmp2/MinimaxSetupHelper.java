package atmp2;

import java.util.List;
import java.util.Scanner;

public class MinimaxSetupHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static int pickAiType() {
        System.out.println("Select AI type");
        System.out.println("1. depth limited alpha beta pruned minimax");
        System.out.println("2. complete alpha beta pruned minimax");
        System.out.println("3. depth limited minimax");
        System.out.println("4. complete minimax");
        System.out.println("5. random moves");
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice < 6 & choice > 0) {
                    return choice - 1;
                }
                System.out.println("Invalid Selection, input a number 1-5");
            } catch (NumberFormatException e) {
                System.out.println("Invalid Selection, input a number 1-5");
            }
        }
    }

    public static final int pickValidAiType(List<Integer> validOpts) {
        for (int i = 1; i < validOpts.size() + 1; i++) {
            System.out.println(i + ". " + Minimax.modes.get(validOpts.get(i - 1)));
        }
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice <= validOpts.size() & choice > 0) {
                    return validOpts.get(choice - 1);
                }
                System.out.println(choice);
                System.out.println("Invalid Selection, input a number 1-" + validOpts.size());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Selection, input a number 1-" + validOpts.size());
            }
        }
    }

    public static int pickDepth() {
        int depth = 5;
        System.out.printf("Enter minimax depth (default %d).\n", depth);
        String input = scanner.nextLine();
        try {
            depth = Integer.parseInt(input);

        } catch (NumberFormatException e) {
            System.out.printf("Invalid integer, using default depth: %d\n", depth);
        }
        return depth;
    }

}
