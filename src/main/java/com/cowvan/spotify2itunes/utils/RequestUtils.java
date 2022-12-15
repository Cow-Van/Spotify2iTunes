package com.cowvan.spotify2itunes.utils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

public final class RequestUtils {
    private RequestUtils() {
    }

    public static HttpResponse<String> getRequest(String url, @Nullable Map<String, String> headers, @Nullable String parameters) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET();

        if (parameters != null) {
            requestBuilder.uri(new URI(url + "?" + parameters));
        } else {
            requestBuilder.uri(new URI(url));
        }

        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBuilder.setHeader(key, headers.get(key));
            }
        }

        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> getRequest(String url, @Nullable Map<String, String> headers, Map<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        return getRequest(url, headers, (parameters == null) ? null : ParseUtils.parseMapToASCIIString(parameters));
    }

    public static HttpResponse<String> postRequest(String url, @Nullable Map<String, String> headers, @Nullable String parameters) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(Objects.requireNonNullElse(parameters, "")));

        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBuilder.setHeader(key, headers.get(key));
            }
        }

        return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> postRequest(String url, @Nullable Map<String, String> headers, Map<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        return postRequest(url, headers, (parameters == null) ? null : ParseUtils.parseMapToJSONString(parameters));
    }
}
