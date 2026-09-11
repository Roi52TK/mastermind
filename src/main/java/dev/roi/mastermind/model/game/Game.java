package dev.roi.mastermind.model.game;

import dev.roi.mastermind.common.GameState;
import dev.roi.mastermind.model.data.Code;
import dev.roi.mastermind.model.data.MatchResult;
import dev.roi.mastermind.model.data.MatchType;
import dev.roi.mastermind.model.logic.CodeGenerator;
import dev.roi.mastermind.model.logic.CodeMatcher;

public class Game {

    private final int codeLength;
    private final int codeOptionsCount;
    private final int maxTries;
    private int currentTry;
    private Code secretCode;
    private final MatchResult[] matchResults;
    private GameState gameState;

    public Game(int codeLength, int codeOptionsCount, int maxTries) {

        if(codeLength <= 0) {
            throw new IllegalArgumentException("Secret Code length must be at least 1");
        }

        if(codeOptionsCount <= 0) {
            throw new IllegalArgumentException("Secret code options count must be at least 1");
        }

        if(maxTries <= 0) {
            throw new IllegalArgumentException("Max tries must be at least 1");
        }

        this.codeLength = codeLength;
        this.codeOptionsCount = codeOptionsCount;
        this.maxTries = maxTries;
        matchResults = new MatchResult[maxTries];
        gameState = GameState.NOT_STARTED;
    }

    public void start() {
        secretCode = CodeGenerator.randomCode(codeLength, codeOptionsCount);
        currentTry = 0;
        gameState = GameState.ONGOING;
    }

    public void guess(Code guess) {

        if(isGameOver()) {
            return;
        }

        if(currentTry >= maxTries) {
            endGameLoss();
            return;
        }

        if(guess.getLength() != codeLength) {
            throw new IllegalArgumentException("Guess must have the same length of the Secret Code");
        }

        if(guess.getOptionsCount() != codeOptionsCount) {
            throw new IllegalArgumentException("Guess must have the same number of options of the Secret Code");
        }

        currentTry++;
        CodeMatcher codeMatcher = new CodeMatcher(secretCode, guess);
        codeMatcher.match();
        matchResults[currentTry - 1] = codeMatcher.getMatchResult();

        if(matchResults[currentTry - 1].isCorrect()) {
            endGameWin();
        }
    }

    public boolean isGameOver() {
        return gameState == GameState.WON || gameState == GameState.LOST;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean hasLastMatchResult() {
        return matchResults[currentTry -1] != null;
    }

    public MatchType getLastMatchResultAt(int index) {
        if(!hasLastMatchResult())
            return null;

        return matchResults[currentTry - 1].get(index);
    }

    public boolean isLastMatchResultCorrect() {
        if(!hasLastMatchResult())
            return false;

        return matchResults[currentTry - 1].isCorrect();
    }

    private void endGameLoss() {
        gameState = GameState.LOST;
    }

    private void endGameWin() {
        gameState = GameState.WON;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public int getCodeOptionsCount() {
        return codeOptionsCount;
    }
}
