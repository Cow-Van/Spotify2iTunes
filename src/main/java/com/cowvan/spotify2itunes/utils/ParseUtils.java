package com.cowvan.spotify2itunes.utils;

import com.cowvan.spotify2itunes.Constants;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;

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

    public static String parseSpotifyLinkToId(String link) {
        if (link == null) {
            return "";
        }

        Matcher matcher = Constants.spotifyLinkPattern.matcher(link);

        if (!matcher.find()) {
            return "";
        }

        return matcher.group(Constants.spotifyLinkPatternIdGroup);
    }
}
