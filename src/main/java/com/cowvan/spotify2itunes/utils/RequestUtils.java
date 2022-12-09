package com.cowvan.spotify2itunes.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public final class RequestUtils {
    private RequestUtils() {
    }

    public static HttpResponse<String> getRequest(String url, Map<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();

        String _url = url + "?" + ParseUtils.parseURLParameters(parameters);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(_url))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> postRequest(String url, Map<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();

        JSONObject requestData = new JSONObject(parameters);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
