package dev.roi.mastermind.model.logic;

import dev.roi.mastermind.model.data.Code;

import java.util.Random;

public final class CodeGenerator {
    private static final Random rand = new Random();

    private CodeGenerator() {}

    public static Code randomCode(int length, int optionsCount) {
        Code code = new Code(length, optionsCount);
        int value;

        for(int i = 0; i < length; i++) {
            value = rand.nextInt(optionsCount);
            code.set(i, value);
        }

        return code;
    }
}
