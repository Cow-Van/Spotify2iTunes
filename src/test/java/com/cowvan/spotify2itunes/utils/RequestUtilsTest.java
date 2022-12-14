package com.cowvan.spotify2itunes.utils;

import org.json.JSONObject;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestUtilsTest {
    @RepeatedTest(5)
    public void test_GetRequest() throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(randomString((int) (Math.random() * 100)), randomString((int) (Math.random() * 100)));
        parameters.put(randomString((int) (Math.random() * 100)), randomString((int) (Math.random() * 100)));
        parameters.put(randomString((int) (Math.random() * 100)), randomString((int) (Math.random() * 100)));

        HttpResponse<String> response = RequestUtils.getRequest("https://httpbin.org/get", parameters);
        JSONObject body = ParseUtils.parseJSONString(String.valueOf(ParseUtils.parseJSONString(response.body()).get("args")));

        for (String key : parameters.keySet()) {
            assertTrue(body.keySet().contains(key));
            assertEquals(body.get(key), parameters.get(key));
        }
    }

    @RepeatedTest(5)
    public void test_PostRequest() throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(randomString((int) (Math.random() * 100)), randomString((int) (Math.random() * 100)));
        parameters.put(randomString((int) (Math.random() * 100)), randomString((int) (Math.random() * 100)));
        parameters.put(randomString((int) (Math.random() * 100)), randomString((int) (Math.random() * 100)));

        HttpResponse<String> response = RequestUtils.postRequest("https://httpbin.org/post", parameters);
        JSONObject body = ParseUtils.parseJSONString(String.valueOf(ParseUtils.parseJSONString(response.body()).get("data")));

        for (String key : parameters.keySet()) {
            assertTrue(body.keySet().contains(key));
            assertEquals(body.get(key), parameters.get(key));
        }
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
}