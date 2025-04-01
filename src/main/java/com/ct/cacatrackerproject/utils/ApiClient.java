package com.ct.cacatrackerproject.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/cacatrackerapi/rest";

    public static String sendPostRequest(String endpoint, String jsonBody) throws IOException {

        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readResponse(connection);
        } else if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return readResponse(connection);
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            System.out.println("Error response: " + responseCode + " BAD LOGIN");
            return readResponse(connection);
        } else {
            System.out.println("Error response: " + responseCode);
            return null;
        }
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        System.out.println("Response Body: " + response);
        return response.toString();
    }
}
