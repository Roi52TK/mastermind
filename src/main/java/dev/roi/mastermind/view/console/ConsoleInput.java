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

    public static int[] readDigitArray(int length) {
        int[] arr = new int[length];
        int index;
        char c;

        while (true) {
            String input = SCANNER.next();

            if(input.length() != length) {
                System.out.println("Length does not match! Please re-enter the sequence.");
                continue;
            }

            for(index = 0; index < length; index++) {
                c = input.charAt(index);
                if(c < '0' || c > '9') {
                    System.out.println("Please enter digits only (without any spaces)!");
                    break;
                }

                arr[index] = c - '0';
            }

            if(index == length)
                return arr;
        }
    }

    public static boolean readYesNo() {
        return SCANNER.next().charAt(0) == 'Y';
    }
}
