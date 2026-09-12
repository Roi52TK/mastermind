package dev.roi.mastermind.view;

import dev.roi.mastermind.common.Difficulty;
import dev.roi.mastermind.common.GameActionError;
import dev.roi.mastermind.common.GameSettings;
import dev.roi.mastermind.controller.Controller;
import dev.roi.mastermind.controller.GameUI;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUI implements GameUI {
    private Controller gameController;
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final char CORRECT_POSITION_SYMBOL = 'V';
    private static final char WRONG_POSITION_SYMBOL = 'O';
    private static final char NONE_SYMBOL = 'X';

    private GameSettings gameSettings;

    public void setController(Controller controller) {
        this.gameController = controller;
    }

    public void run() {
        System.out.println("Starting Console UI for Mastermind game...");
        startGame();
    }

    private void startGame() {
        initGameSettings();
        printGameSettings();
        printGameInstructions();
        gameController.startNewGame(gameSettings);
    }

    private void printGameSettings() {
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

    private void printGameInstructions() {
        System.out.println("""
                V = Correct position
                O = Wrong position
                X = Not in code
                ~~~~~~~~~~~~~~~~~~~~~~
                """);
    }

    private void initGameSettings() {
        int choice;
        Difficulty difficulty;
        System.out.println("Choose difficulty");
        System.out.println("""
                0 = Custom
                1 = Easy
                2 = Medium
                3 = Hard""");

        while(true) {
            try {
                choice = SCANNER.nextInt();

                switch (choice) {
                    case 0:
                        initCustomGameSettings();
                        return;
                    case 1:
                        difficulty = Difficulty.EASY;
                        break;
                    case 2:
                        difficulty = Difficulty.MEDIUM;
                        break;
                    case 3:
                        difficulty = Difficulty.HARD;
                        break;
                    default:
                        System.out.println("Invalid answer! Reinitializing game settings...\n");
                        continue;
                }

                break;
            } catch (InputMismatchException e) {
                SCANNER.next();
                System.out.println("Invalid value! Please enter a number (0-3).");
            }
        }

        gameSettings = difficulty.getGameSettings();
    }

    private void initCustomGameSettings() {
        int codeLength;
        int codeOptionsCount;
        int maxTries;

        System.out.println("~~~~Input custom game settings~~~~");

        while (true) {
            try {
                System.out.print("Enter code length: ");
                codeLength = SCANNER.nextInt();
                System.out.print("Enter code options count: ");
                codeOptionsCount = SCANNER.nextInt();
                System.out.print("Enter max tries: ");
                maxTries = SCANNER.nextInt();

                break;

            } catch (InputMismatchException e) {
                SCANNER.next();
                System.out.println("Invalid value! Restarting input settings...");
            }
        }

        gameSettings = new GameSettings(codeLength, codeOptionsCount, maxTries);
    }

    private void printSecretCodePattern() {
        for(int i = 0; i < gameController.getCodeLength(); i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    private void printLastMatchResult() {
        char symbol;
        int codeLength = gameController.getCodeLength();
        int[] lastGuess = gameController.getLastGuess();

        System.out.print("Guess:   ");
        for(int i = 0; i < codeLength; i++) {
            System.out.print(lastGuess[i] + " ");
        }

        System.out.print("\nResult:  ");
        for(int i = 0; i < codeLength; i++) {
            symbol = switch (gameController.getLastMatchResultAt(i)) {
                case CORRECT_POSITION -> CORRECT_POSITION_SYMBOL;
                case WRONG_POSITION -> WRONG_POSITION_SYMBOL;
                case NONE -> NONE_SYMBOL;
            };
            System.out.print(symbol + " ");
        }
        System.out.println();
    }

    //TODO: handle out of range value input case
    private int[] scanGuess() {
        int[] guess = new int[gameController.getCodeLength()];

        System.out.println("Input your guess (press enter after each value)");
        System.out.println("values range [0-" + (gameController.getCodeOptionsCount() - 1) +
                "] ------ guess " + (gameController.getCurrentTry() + 1) + "/" + gameController.getMaxTries());

        while (true) {
            try {
                for (int i = 0; i < guess.length; i++) {
                    guess[i] = SCANNER.nextInt();
                }

                return guess;

            } catch (InputMismatchException e) {
                SCANNER.next();
                System.out.println("Invalid value! Please enter a number.");
            }
        }
    }

    private void onGameOver() {
        playAgainDialog();
    }

    private void playAgainDialog() {
        boolean playAgain;
        System.out.print("\nPlay again? (Y/N): ");
        playAgain = SCANNER.next().charAt(0) == 'Y';

        if(playAgain) {
            startGame();
        }
        else {
            System.out.println("\nExiting...");
        }
    }

    @Override
    public void onGameStart() {
        System.out.println("Game has started!");
        System.out.println("Will you be able to crack the code?");
        printSecretCodePattern();
        int[] guess = scanGuess();
        gameController.guess(guess);
    }

    @Override
    public void onNextGuess() {
        printLastMatchResult();
        int[] guess = scanGuess();
        gameController.guess(guess);
    }

    @Override
    public void onGameWon() {
        printLastMatchResult();
        System.out.println("\nCongratulations! You won!");
        onGameOver();
    }

    @Override
    public void onGameLost() {
        printLastMatchResult();
        System.out.println("\nNot enough tries, huh?");
        onGameOver();
    }

    @Override
    public void onInvalidInteraction(GameActionError error) {
        System.out.println("ERROR: " + error);
    }
}
