package dev.roi.mastermind.view.console;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class ConsoleInput {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleInput() {}

    public static int readInt() {
        int num;

        while (true) {
            try {
                num = SCANNER.nextInt();
                return num;

            } catch (InputMismatchException e) {
                SCANNER.next();
                System.out.println("Invalid value! Please enter a number.");
            }
        }
    }

    public static int[] readIntArray(int length) {
        int[] arr = new int[length];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = readInt();
        }

        return arr;
    }

    public static boolean readYesNo() {
        return SCANNER.next().charAt(0) == 'Y';
    }
}
