package com.cowvan.spotify2itunes.command.argument;

public class Word implements Argument {
    private final String word;

    private Word(String word) {
        if (word.length() < 1) {
            throw new IllegalArgumentException("Word cannot be empty");
        }

        this.word = word;
    }

    public static Word literal(String word) {
        return new Word(word);
    }

    @Override
    public String asString() {
        return word;
    }
}
