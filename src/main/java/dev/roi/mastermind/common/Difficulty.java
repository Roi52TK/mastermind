package dev.roi.mastermind.common;

public enum Difficulty {
    EASY(4, 6, 10),
    MEDIUM(5, 8, 8),
    HARD(6, 10, 7);

    private final GameSettings gameSettings;

    Difficulty(int codeLength, int codeOptionsCount, int maxTries) {
        this.gameSettings = new GameSettings(codeLength, codeOptionsCount, maxTries);
    }

    public GameSettings getGameSettings() {
        return this.gameSettings;
    }
}
