package dev.roi.mastermind.model;

public class MatchResult {

    private final MatchType[] result;
    private final int length;

    public MatchResult(int length) {
        this.length = length;
        this.result = new MatchType[length];
    }

    public boolean isCorrect() {
        for(int i = 0; i < length; i++) {
            if(result[i] != MatchType.CORRECT_POSITION)
                return false;
        }

        return true;
    }

    public MatchType get(int index) {
        return result[index];
    }

    public void set(int index, MatchType value) {
        result[index] = value;
    }
}
