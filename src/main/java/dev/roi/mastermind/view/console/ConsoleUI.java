package dev.roi.mastermind.view.console;

import dev.roi.mastermind.common.Difficulty;
import dev.roi.mastermind.common.GameActionError;
import dev.roi.mastermind.common.GameSettings;
import dev.roi.mastermind.controller.Controller;
import dev.roi.mastermind.controller.GameUI;

public class ConsoleUI implements GameUI {
    private Controller controller;

    private GameSettings gameSettings;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println("Starting Console UI for Mastermind game...");
        startNewGame();
    }

    private void startNewGame() {
        System.out.println("Starting new game!");
        initGameSettings();
        controller.startNewGame(gameSettings);
    }

    private void restartGame() {
        System.out.println("Restarting game!");
        controller.startNewGame(gameSettings);
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
        controller.guess(guess);
    }

    private int[] scanGuess() {
        int codeOptionsCount = controller.getCodeOptionsCount();

        if(codeOptionsCount <= 10) {
            System.out.println("Input your guess in one line without spaces");
            System.out.println("values range [0-" + (controller.getCodeOptionsCount() - 1) +
                    "] ------ guess " + (controller.getCurrentTry() + 1) + "/" + controller.getMaxTries());

            return ConsoleInput.readDigitArray(controller.getCodeLength());
        }
        else {
            System.out.println("Input your guess (press enter after each value)");
            System.out.println("values range [0-" + (controller.getCodeOptionsCount() - 1) +
                    "] ------ guess " + (controller.getCurrentTry() + 1) + "/" + controller.getMaxTries());

            return ConsoleInput.readIntArray(controller.getCodeLength());
        }
    }

    private void onGameOver() {
        System.out.println("Secret code: " + ConsoleOutput.intArrToString(controller.getSecretCode()));
        playAgainDialog();
    }

    private void playAgainDialog() {
        boolean isPlayAgain;
        System.out.print("\nPlay again? (Y/N): ");
        isPlayAgain = ConsoleInput.readYesNo();

        if(isPlayAgain) {
            playAgain();
        }
        else {
            System.out.println("\nExiting...");
        }
    }

    private void playAgain() {
        boolean isSameSettings;
        System.out.println("Playing again!");
        System.out.print("Same settings? (Y/N): ");
        isSameSettings = ConsoleInput.readYesNo();

        if(isSameSettings) {
            restartGame();
        }
        else {
            startNewGame();
        }
    }

    private void printLastMatchResult() {
        ConsoleOutput.printMatchResult(controller.getLastGuess(), controller.getLastMatchResult());
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
                System.out.println("ERROR: Invalid guess value! Rescanning guess...");
                guess();
            }
            case INVALID_GAME_SETTINGS -> {
                System.out.println("ERROR: Invalid game settings! Restarting game...");
                startNewGame();
            }
            default -> System.out.println("ERROR: " + error);
        }
    }
}
