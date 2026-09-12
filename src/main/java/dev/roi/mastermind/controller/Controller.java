package dev.roi.mastermind.controller;

import dev.roi.mastermind.common.GameActionError;
import dev.roi.mastermind.common.GameSettings;
import dev.roi.mastermind.common.GameState;
import dev.roi.mastermind.model.data.Code;
import dev.roi.mastermind.model.data.MatchType;
import dev.roi.mastermind.model.game.Game;

public class Controller {

    private Game game;
    private final GameUI gameUI;

    public Controller(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    public void startNewGame(GameSettings gameSettings) {
        try {
            game = new Game(gameSettings);
            game.start();
            gameUI.onGameStart();
        } catch (IllegalArgumentException e) {
            gameUI.onInvalidInteraction(GameActionError.INVALID_GAME_SETTINGS);
        }
    }

    public void guess(int[] guess) {

        if(game == null) {
            gameUI.onInvalidInteraction(GameActionError.GAME_NOT_STARTED);
            return;
        }

        if(game.getGameState() != GameState.ONGOING) {
            gameUI.onInvalidInteraction(GameActionError.GAME_ALREADY_ENDED);
            return;
        }

        if(guess.length != game.getCodeLength()) {
            gameUI.onInvalidInteraction(GameActionError.INVALID_GUESS_LENGTH);
            return;
        }

        Code guessCode = new Code(game.getCodeLength(), game.getCodeOptionsCount());
        try {
            for(int i = 0; i < guessCode.getLength(); i++) {

                guessCode.set(i, guess[i]);

            }
        } catch (IllegalArgumentException e) {
            gameUI.onInvalidInteraction(GameActionError.INVALID_GUESS_VALUE);
            return;
        }

        game.guess(guessCode);

        switch (game.getGameState()) {
            case WON -> gameUI.onGameWon();
            case LOST -> gameUI.onGameLost();
            case ONGOING -> gameUI.onNextGuess();
        }
    }

    public int getCodeLength() {
        return game.getCodeLength();
    }

    public int getCodeOptionsCount() {
        return game.getCodeOptionsCount();
    }

    public int getMaxTries() {
        return game.getMaxTries();
    }

    public int getCurrentTry() {
        return game.getCurrentTry();
    }

    public MatchType getLastMatchResultAt(int index) {
        return game.getLastMatchResultAt(index);
    }

    public int[] getLastGuess() {
        return game.getLastGuess();
    }
}
