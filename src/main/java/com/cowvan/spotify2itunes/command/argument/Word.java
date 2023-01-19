package com.cowvan.spotify2itunes.command.argument;

public class Word implements Argument {
    // TODO
    private Word(String flag) {

    }

    public static Word literal(String word) {
        return new Word(word);
    }

    // TODO
    @Override
    public String asString() {
        return "";
    }
}
