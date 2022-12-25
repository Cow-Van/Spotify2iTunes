package com.cowvan.spotify2itunes.utils;

import org.json.JSONObject;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestUtilsTest {
    @RepeatedTest(5)
    public void test_GetRequest_ParametersOnly() throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(randomString((int) (Math.random() * 100) + 1), randomString((int) (Math.random() * 100) + 1));
        parameters.put(randomString((int) (Math.random() * 100) + 1), randomString((int) (Math.random() * 100) + 1));
        parameters.put(randomString((int) (Math.random() * 100) + 1), randomString((int) (Math.random() * 100) + 1));

        HttpResponse<String> response = RequestUtils.getRequest("https://httpbin.org/get", null, parameters);
        JSONObject data = ParseUtils.parseJSONStringToJSONObject(response.body()).getJSONObject("args");

        for (String key : parameters.keySet()) {
            assertTrue(data.keySet().contains(key));
            assertEquals(data.get(key), parameters.get(key));
        }
    }

    @RepeatedTest(5)
    public void test_PostRequest_ParametersOnly() throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(randomString((int) (Math.random() * 100) + 1), randomString((int) (Math.random() * 100) + 1));
        parameters.put(randomString((int) (Math.random() * 100) + 1), randomString((int) (Math.random() * 100) + 1));
        parameters.put(randomString((int) (Math.random() * 100) + 1), randomString((int) (Math.random() * 100) + 1));

        HttpResponse<String> response = RequestUtils.postRequest("https://httpbin.org/post", null, parameters);
        JSONObject data = ParseUtils.parseJSONStringToJSONObject(response.body()).getJSONObject("json");

        for (String key : parameters.keySet()) {
            assertTrue(data.keySet().contains(key));
            assertEquals(data.get(key), parameters.get(key));
        }
    }

    @RepeatedTest(5)
    public void test_GetRequest_HeadersOnly() throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put(StringUtils.capitalize(randomString(97, 122, (int) (Math.random() * 100) + 1)), randomString(97, 122, (int) (Math.random() * 100) + 1));
        headers.put(StringUtils.capitalize(randomString(97, 122, (int) (Math.random() * 100) + 1)), randomString(97, 122, (int) (Math.random() * 100) + 1));
        headers.put(StringUtils.capitalize(randomString(97, 122, (int) (Math.random() * 100) + 1)), randomString(97, 122, (int) (Math.random() * 100) + 1));

        HttpResponse<String> response = RequestUtils.getRequest("https://httpbin.org/anything", headers, (Map<String, String>) null);

        if (response.statusCode() == 502) {
            return;
        }

        JSONObject responseRequestHeaders = ParseUtils.parseJSONStringToJSONObject(ParseUtils.parseJSONStringToJSONObject(response.body()).get("headers").toString()); // Headers sent in the request that httpbin.org is

        for (String key : headers.keySet()) {
            assertEquals(responseRequestHeaders.get(key), headers.get(key));
        }
    }

    @RepeatedTest(5)
    public void test_PostRequest_HeadersOnly() throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put(StringUtils.capitalize(randomString(97, 122, (int) (Math.random() * 100) + 1)), randomString(97, 122, (int) (Math.random() * 100) + 1));
        headers.put(StringUtils.capitalize(randomString(97, 122, (int) (Math.random() * 100) + 1)), randomString(97, 122, (int) (Math.random() * 100) + 1));
        headers.put(StringUtils.capitalize(randomString(97, 122, (int) (Math.random() * 100) + 1)), randomString(97, 122, (int) (Math.random() * 100) + 1));

        HttpResponse<String> response = RequestUtils.postRequest("https://httpbin.org/anything", headers, (Map<String, String>) null);
        JSONObject responseRequestHeaders = ParseUtils.parseJSONStringToJSONObject(ParseUtils.parseJSONStringToJSONObject(response.body()).get("headers").toString()); // Headers sent in the request that httpbin.org is
        // sending back in the response body
        for (String key : headers.keySet()) {
            assertEquals(responseRequestHeaders.get(key), headers.get(key));
        }
    }

    @Test
    public void test_GetRequest_Null() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = RequestUtils.getRequest("https://httpbin.org/get", null, (Map<String, String>) null);

        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void test_PostRequest_Null() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = RequestUtils.postRequest("https://httpbin.org/post", null, (Map<String, String>) null);

        assertEquals(response.statusCode(), 200);
    }

    private String randomString(int length) {
        int min = 32;
        int max = 126;

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (Math.random() * (max - min) + min));
        }

        return stringBuilder.toString();
    }

    private String randomString(int minChar, int maxChar, int length) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (Math.random() * (maxChar - minChar) + minChar));
        }

        return stringBuilder.toString();
    }
}