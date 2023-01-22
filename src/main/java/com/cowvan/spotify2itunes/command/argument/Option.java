package com.cowvan.spotify2itunes.command.argument;

public class Option implements Argument {
    private final String option;
    private final String value;

    private Option(String option, String value) {
        if (option.length() < 1 || value.length() < 1) {
            throw new IllegalArgumentException("Option or value cannot be empty");
        }

        this.option = option;
        this.value = value;
    }

    public static Option literal(String option, String value) {
        return new Option(option, value);
    }

    @Override
    public String asString() {
        return option + " " + value;
    }
}
