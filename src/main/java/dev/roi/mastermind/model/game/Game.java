package dev.roi.mastermind.model.game;

import dev.roi.mastermind.common.GameSettings;
import dev.roi.mastermind.common.GameState;
import dev.roi.mastermind.model.data.Code;
import dev.roi.mastermind.model.data.MatchResult;
import dev.roi.mastermind.common.MatchType;
import dev.roi.mastermind.model.logic.CodeGenerator;
import dev.roi.mastermind.model.logic.CodeMatcher;

public class Game {

    private final GameSettings gameSettings;
    private int currentTry;
    private Code secretCode;
    private final Code[] guesses;
    private final MatchResult[] matchResults;
    private GameState gameState;

    public Game(GameSettings gameSettings) {

        if(gameSettings.codeLength() <= 0) {
            throw new IllegalArgumentException("Secret Code length must be at least 1");
        }

        if(gameSettings.codeOptionsCount() <= 0) {
            throw new IllegalArgumentException("Secret code options count must be at least 1");
        }

        if(gameSettings.maxTries() <= 0) {
            throw new IllegalArgumentException("Max tries must be at least 1");
        }

        this.gameSettings = gameSettings;
        guesses = new Code[gameSettings.maxTries()];
        matchResults = new MatchResult[gameSettings.maxTries()];
        gameState = GameState.NOT_STARTED;
    }

    public void start() {
        secretCode = CodeGenerator.randomCode(gameSettings.codeLength(), gameSettings.codeOptionsCount());
        currentTry = 0;
        gameState = GameState.ONGOING;
    }

    public void guess(Code guess) {

        if(isGameOver()) {
            return;
        }

        if(currentTry >= gameSettings.maxTries()) {
            endGameLoss();
            return;
        }

        if(guess.getLength() != gameSettings.codeLength()) {
            throw new IllegalArgumentException("Guess must have the same length of the Secret Code");
        }

        if(guess.getOptionsCount() != gameSettings.codeOptionsCount()) {
            throw new IllegalArgumentException("Guess must have the same number of options of the Secret Code");
        }

        currentTry++;
        CodeMatcher codeMatcher = new CodeMatcher(secretCode, guess);
        codeMatcher.match();
        guesses[currentTry - 1] = guess;
        matchResults[currentTry - 1] = codeMatcher.getMatchResult();

        if(matchResults[currentTry - 1].isCorrect()) {
            endGameWin();
        }
    }

    private void endGameLoss() {
        gameState = GameState.LOST;
    }

    private void endGameWin() {
        gameState = GameState.WON;
    }

    public boolean isGameOver() {
        return gameState == GameState.WON || gameState == GameState.LOST;
    }

    public GameState getGameState() {
        return gameState;
    }

    public MatchType[] getMatchResult(int tryNum) {
        MatchType[] resultCopy;

        if(tryNum <= 0 || tryNum > currentTry)
            return null;

        resultCopy = new MatchType[gameSettings.codeLength()];

        for(int i = 0; i < gameSettings.codeLength(); i++) {
            resultCopy[i] = matchResults[tryNum - 1].get(i);
        }

        return resultCopy;
    }

    public MatchType[] getLastMatchResult() {
        return getMatchResult(currentTry);
    }

    public int[] getGuess(int tryNum) {
        int[] guessCopy;

        if(tryNum <= 0 || tryNum > currentTry)
            return null;

        guessCopy = new int[gameSettings.codeLength()];

        for(int i = 0; i < gameSettings.codeLength(); i++) {
            guessCopy[i] = guesses[tryNum - 1].get(i);
        }

        return guessCopy;
    }

    public int[] getLastGuess() {
        return getGuess(currentTry);
    }

    public int getCodeLength() {
        return gameSettings.codeLength();
    }

    public int getCodeOptionsCount() {
        return gameSettings.codeOptionsCount();
    }

    public int getMaxTries() {
        return gameSettings.maxTries();
    }

    public int getCurrentTry() {
        return currentTry;
    }
}
