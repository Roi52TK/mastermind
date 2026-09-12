package dev.roi.mastermind.model.logic;

import dev.roi.mastermind.model.data.Code;
import dev.roi.mastermind.model.data.MatchResult;
import dev.roi.mastermind.common.MatchType;

public class CodeMatcher {

    private final Code secretCode;
    private final Code guess;
    private final int codeLength;
    private final int codeOptionsCount;
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
        this.codeOptionsCount = secretCode.getOptionsCount();
        this.matchResult = new MatchResult(codeLength);
    }

    public void match() {
        int[] secretCodeCountArr = new int[codeOptionsCount];

        // Initial count array for Secret Code
        for(int i = 0; i < codeLength; i++) {
            secretCodeCountArr[secretCode.get(i)]++;
        }

        // Check for Correct Position matches
        for(int i = 0; i < codeLength; i++) {
            if(guess.get(i) == secretCode.get(i)) {
                secretCodeCountArr[secretCode.get(i)]--;

                matchResult.set(i, MatchType.CORRECT_POSITION);
            }
        }

        // Check for Wrong Position matches
        for(int i = 0; i < codeLength; i++) {
            // Check only incorrect values
            if(matchResult.get(i) != MatchType.CORRECT_POSITION) {
                // Check if value exists in secret code - correct positions exclusive
                if(secretCodeCountArr[guess.get(i)] > 0) {
                    secretCodeCountArr[guess.get(i)]--;
                    matchResult.set(i, MatchType.WRONG_POSITION);
                }
                else {
                    // Value does not exist anywhere in the secret code
                    matchResult.set(i, MatchType.NONE);
                }
            }
        }
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }
}
