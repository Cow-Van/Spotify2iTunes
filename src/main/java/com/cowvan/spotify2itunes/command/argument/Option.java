package com.cowvan.spotify2itunes.command.argument;

public class Option implements Argument {
    // TODO
    private Option(String option, String value) {

    }

    public static Option literal(String option, String value) {
        return new Option(option, value);
    }

    // TODO
    @Override
    public String asString() {
        return null;
    }
}
