package com.cowvan.spotify2itunes.command.argument;

import com.cowvan.spotify2itunes.Spotify2iTunes;

public class Flag implements Argument {
    private final String flag;

    private Flag(String flag) {
        if (flag.length() < 1) {
            throw new IllegalArgumentException("Flag cannot be empty");
        }

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
