package dev.roi.mastermind.controller;

import dev.roi.mastermind.common.GameActionError;
import dev.roi.mastermind.common.GameState;
import dev.roi.mastermind.model.data.Code;
import dev.roi.mastermind.model.game.Game;

public class Controller {

    private Game game;
    private final GameUI gameUI;

    public Controller(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    public void startNewGame(int codeLength, int codeOptionsCount, int maxTries) {
        try {
            game = new Game(codeLength, codeOptionsCount, maxTries);
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
        for(int i = 0; i < guessCode.getLength(); i++) {
            guessCode.set(i, guess[i]);
        }

        game.guess(guessCode);

        switch (game.getGameState()) {
            case WON -> gameUI.onGameWon();
            case LOST -> gameUI.onGameLost();
        }
    }
}
