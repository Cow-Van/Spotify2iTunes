package com.cowvan.spotify2itunes.utils;

import com.cowvan.spotify2itunes.Constants;

public final class StringUtils {
    private StringUtils() {
    }

    public static String capitalize(String string) {
        if (string.length() < 1) {
            return string;
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String removeIllegalFilenameCharacters(String string) {
        return string.replaceAll(Constants.illegalFilenameCharactersPattern, "_");
    }

    public static String[] removeIllegalFilenameCharactersFromStrings(String[] strings) {
        String[] legalStrings = new String[strings.length];

        for (int i = 0; i < strings.length; i++) {
            legalStrings[i] = removeIllegalFilenameCharacters(strings[i]);
        }

        return legalStrings;
    }
}
