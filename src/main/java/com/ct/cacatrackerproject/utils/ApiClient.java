package com.ct.cacatrackerproject.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ApiClient {

    //private static final String BASE_URL = "https://8602-2a0c-5a82-150e-cf00-b8de-25a0-bab1-25e.ngrok-free.app/cacatrackerapi/rest";
    private static final String BASE_URL = "http://localhost:8080/cacatrackerapi/rest";
    //private static final String BASE_URL = "http://cacatrackerapi.fly.dev/rest";

    public static String sendPostRequest(String endpoint, String jsonBody) throws IOException {
        try {
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
            System.out.println("1Response Code: " + responseCode);

            String responseBody = readResponse(connection);
            System.out.println("2Response Body: " + responseBody);
            return responseBody;
        } catch (IOException e) {
            System.err.println("3Error during HTTP request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static <T> ArrayList<T> sendGetRequest(String endpoint, Class<T> clazz) throws IOException {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code: " + responseCode);

            String responseBody = readResponse(connection);
            System.out.println("GET Response Body: " + responseBody);

            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<T> result = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
            return result;

        } catch (IOException e) {
            System.err.println("Error during HTTP GET request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static  String sendGetStringRequest(String endpoint) throws IOException {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code: " + responseCode);

            String responseBody = readResponse(connection);
            System.out.println("GET Response Body: " + responseBody);

            return responseBody;

        } catch (IOException e) {
            System.err.println("Error during HTTP GET request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream())
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            System.err.println("4Error reading response: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("5RESPUESTA SERVIDOR RESPONSE METHOD");
        System.out.println("6Response Body: " + response);
        return response.toString();
    }
}

