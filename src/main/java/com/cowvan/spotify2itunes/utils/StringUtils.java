package com.cowvan.spotify2itunes.utils;

public final class StringUtils {
    private StringUtils() {
    }

    public static String capitalize(String string) {
        if (string.length() < 1) {
            return string;
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
