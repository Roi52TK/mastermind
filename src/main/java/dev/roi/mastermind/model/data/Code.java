package dev.roi.mastermind.model.data;

public class Code {

    private final int[] code;
    private final int length;
    private final int optionsCount;

    public Code(int length, int optionsCount) {
        this.code = new int[length];
        this.length = length;
        this.optionsCount = optionsCount;
    }

    public int get(int index) {
        return code[index];
    }

    public void set(int index, int value) {
        if(value < 0 || value >= optionsCount)
            throw new IllegalArgumentException("Invalid value for the code");

        code[index] = value;
    }

    public int getLength() {
        return length;
    }

    public int getOptionsCount() {
        return optionsCount;
    }
}
