package dev.roi.mastermind.view;

import dev.roi.mastermind.common.GameActionError;
import dev.roi.mastermind.controller.Controller;
import dev.roi.mastermind.controller.GameUI;

import java.util.Scanner;

public class ConsoleUI implements GameUI {
    private Controller gameController;
    private static final Scanner SCANNER = new Scanner(System.in);

    public void setController(Controller controller) {
        this.gameController = controller;
    }

    public void run() {
        System.out.println("Starting Console UI for Mastermind game...");
        initGameSettings();
    }

    private void initGameSettings() {
        int codeLength = 1;
        int codeOptionsCount = 1;
        int maxTries = 1;

        System.out.println("~~~~Input game settings~~~~");

        try {
            System.out.print("Enter code length: ");
            codeLength = SCANNER.nextInt();
            System.out.print("Enter code options count: ");
            codeOptionsCount = SCANNER.nextInt();
            System.out.print("Enter max tries: ");
            maxTries = SCANNER.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input! Restarting initialization...");
            initGameSettings();
        }

        gameController.startNewGame(codeLength, codeOptionsCount, maxTries);
    }

    private void printSecretCodePattern() {
        for(int i = 0; i < gameController.getCodeLength(); i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    private void printLastMatchResult() {
        char symbol = 'X';
        for(int i = 0; i < gameController.getCodeLength(); i++) {
            switch (gameController.getLastMatchResultAt(i)) {
                case CORRECT_POSITION -> symbol = 'V';
                case WRONG_POSITION -> symbol = 'O';
                case NONE -> symbol = 'X';
            }
            System.out.print(symbol);
        }
        System.out.println();
    }

    private int[] scanGuess() {
        int[] guess = new int[gameController.getCodeLength()];

        System.out.println("Input your guess (press enter after each value)");
        System.out.println("values range [0-" + (gameController.getCodeOptionsCount() - 1) +
                "] ------ guess " + (gameController.getCurrentTry() + 1) + "/" + gameController.getMaxTries());
        for(int i = 0; i < guess.length; i++) {
            guess[i] = SCANNER.nextInt();
        }

        return guess;
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
    }

    @Override
    public void onGameLost() {
        printLastMatchResult();
        System.out.println("\nNot enough tries, huh?");
    }

    @Override
    public void onInvalidInteraction(GameActionError error) {
        System.out.println(error);
    }
}
