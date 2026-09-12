package dev.roi.mastermind.view.console;

import dev.roi.mastermind.common.GameSettings;
import dev.roi.mastermind.common.MatchType;

public class ConsoleOutput {

    private static final char CORRECT_POSITION_SYMBOL = 'V';
    private static final char WRONG_POSITION_SYMBOL = 'O';
    private static final char NONE_SYMBOL = 'X';

    private ConsoleOutput() {}

    public static void printGameSettings(GameSettings gameSettings) {
        System.out.printf(
                """
                        ~~~~ Game Settings ~~~~
                        Code length: %d
                        Options: 0-%d
                        Max tries: %d
                        ~~~~~~~~~~~~~~~~~~~~~~
                        """, gameSettings.codeLength(),
                gameSettings.codeOptionsCount() - 1,
                gameSettings.maxTries()
        );
    }

    public static void printGameInstructions() {
        System.out.println("""
                V = Correct position
                O = Wrong position
                X = Not in code
                ~~~~~~~~~~~~~~~~~~~~~~
                """);
    }

    public static void printSecretCodePattern(int codeLength) {
        for(int i = 0; i < codeLength; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    public static void printMatchResult(int[] guess, MatchType[] matchTypes) {
        printGuess(guess);
        printMatchResult(matchTypes);
        System.out.println();
    }

    private static void printGuess(int[] guess) {
        System.out.print("Guess:   ");
        for (int i : guess) {
            System.out.print(i + " ");
        }
    }

    private static void printMatchResult(MatchType[] matchTypes) {
        char symbol;
        System.out.print("\nResult:  ");
        for (MatchType matchType : matchTypes) {
            symbol = switch (matchType) {
                case CORRECT_POSITION -> CORRECT_POSITION_SYMBOL;
                case WRONG_POSITION -> WRONG_POSITION_SYMBOL;
                case NONE -> NONE_SYMBOL;
            };
            System.out.print(symbol + " ");
        }
    }
}
