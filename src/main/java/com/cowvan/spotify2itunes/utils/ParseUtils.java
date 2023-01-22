package com.cowvan.spotify2itunes.utils;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class ParseUtils {
    private ParseUtils() {}

    public static JSONObject parseJSONStringToJSONObject(String JSONString) {
        return new JSONObject(JSONString);
    }

    public static String parseMapToASCIIString(Map<String, String> parameters) {
        StringBuilder parameterStringBuilder = new StringBuilder();

        for (String key : parameters.keySet()) {
            parameterStringBuilder
                    .append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                    .append("&");
        }

        return parameterStringBuilder.toString();
    }

    public static String parseMapToJSONString(Map<?, ?> map) {
        return new JSONObject(map).toString();
    }

    public static String parseSpotifyLinkToSpotifyId(String link) {
        String[] linkSegments = link.split("/");
        return linkSegments[linkSegments.length - 1].split("\\?")[0];
    }
}
