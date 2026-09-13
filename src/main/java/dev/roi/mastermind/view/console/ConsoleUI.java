package dev.roi.mastermind.view.console;

import dev.roi.mastermind.common.Difficulty;
import dev.roi.mastermind.common.GameActionError;
import dev.roi.mastermind.common.GameSettings;
import dev.roi.mastermind.controller.Controller;
import dev.roi.mastermind.controller.GameUI;

public class ConsoleUI implements GameUI {
    private Controller gameController;

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
        gameController.startNewGame(gameSettings);
    }

    private void initGameSettings() {
        int choice;
        Difficulty difficulty;
        System.out.println("""
                ~~~~~~~ Choose difficulty ~~~~~~~
                            0 = Custom
                            1 = Easy
                            2 = Medium
                            3 = Hard
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");

        while(true) {
            System.out.print("Enter 0/1/2/3: ");
            choice = ConsoleInput.readInt();

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
                    System.out.println("Invalid answer! Values 0-3\n");
                    continue;
            }

            break;
        }

        gameSettings = difficulty.getGameSettings();
    }

    private void initCustomGameSettings() {
        int codeLength;
        int codeOptionsCount;
        int maxTries;

        System.out.println("~~~~Input custom game settings~~~~");

        System.out.print("Enter code length: ");
        codeLength = ConsoleInput.readInt();
        System.out.print("Enter code options count: ");
        codeOptionsCount = ConsoleInput.readInt();
        System.out.print("Enter max tries: ");
        maxTries = ConsoleInput.readInt();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        gameSettings = new GameSettings(codeLength, codeOptionsCount, maxTries);
    }

    private void guess() {
        int[] guess = scanGuess();
        gameController.guess(guess);
    }

    private int[] scanGuess() {
        int codeOptionsCount = gameController.getCodeOptionsCount();

        if(codeOptionsCount <= 10) {
            System.out.println("Input your guess in one line without spaces");
            System.out.println("values range [0-" + (gameController.getCodeOptionsCount() - 1) +
                    "] ------ guess " + (gameController.getCurrentTry() + 1) + "/" + gameController.getMaxTries());

            return ConsoleInput.readDigitArray(gameController.getCodeLength());
        }
        else {
            System.out.println("Input your guess (press enter after each value)");
            System.out.println("values range [0-" + (gameController.getCodeOptionsCount() - 1) +
                    "] ------ guess " + (gameController.getCurrentTry() + 1) + "/" + gameController.getMaxTries());

            return ConsoleInput.readIntArray(gameController.getCodeLength());
        }
    }

    private void onGameOver() {
        System.out.println("Secret code: " + ConsoleOutput.intArrToString(gameController.getSecretCode()));
        playAgainDialog();
    }

    private void playAgainDialog() {
        boolean playAgain;
        System.out.print("\nPlay again? (Y/N): ");
        playAgain = ConsoleInput.readYesNo();

        if(playAgain) {
            startGame();
        }
        else {
            System.out.println("\nExiting...");
        }
    }

    private void printLastMatchResult() {
        ConsoleOutput.printMatchResult(gameController.getLastGuess(), gameController.getLastMatchResult());
    }

    @Override
    public void onGameStart() {
        System.out.println();
        ConsoleOutput.printGameSettings(gameSettings);
        ConsoleOutput.printGameInstructions();
        System.out.print("""
                        Game has started!
                Will you be able to crack the code?
                """);
        ConsoleOutput.printSecretCodePattern(gameSettings.codeLength());
        guess();
    }

    @Override
    public void onNextGuess() {
        printLastMatchResult();
        guess();
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
        switch (error) {
            case INVALID_GUESS_VALUE -> {
                System.out.println("ERROR: Invalid guess value! Rescanning guess.");
                guess();
            }
            default -> System.out.println("ERROR: " + error);
        }
    }
}
