package com.cowvan.spotify2itunes.command.argument;

public class Flag implements Argument {
    private final String flag;

    private Flag(String flag) {
        this.flag = flag;
    }

    public static Flag literal(String flag) {
        return new Flag(flag);
    }

    @Override
    public String asString() {
        return flag;
    }
}
