package com.cowvan.spotify2itunes.command.argument;

public class Flag implements Argument {
    // TODO
    private Flag(String flag) {

    }

    public static Flag literal(String flag) {
        return new Flag(flag);
    }

    // TODO
    @Override
    public String asString() {
        return "";
    }
}
