package dev.roi.mastermind.model;

public class CodeMatcher {

    private final Code secretCode;
    private final Code guess;
    private final int codeLength;
    private final MatchResult matchResult;

    public CodeMatcher(Code secretCode, Code guess) {
        if (secretCode.getLength() != guess.getLength()) {
            throw new IllegalArgumentException("Codes must have the same length");
        }

        if (secretCode.getOptionsCount() != guess.getOptionsCount()) {
            throw new IllegalArgumentException("Codes must have the same number of options");
        }

        this.secretCode = secretCode;
        this.guess = guess;
        this.codeLength = secretCode.getLength();
        this.matchResult = new MatchResult(codeLength);
    }

    public void match() {

    }

    public MatchResult getMatchResult() {
        return matchResult;
    }
}
