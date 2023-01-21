package com.cowvan.spotify2itunes.command.argument;

public class Word implements Argument {
    private final String word;

    private Word(String word) {
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
