package com.eas.app.api;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BaseApi {

    public static class TokenResponse {
        public String accessToken;
        public String refreshToken;
    }

    public TokenResponse peticionPOST(String endpoint, String... params) throws Exception {
        URL url = new URL("https://tuapi.com" + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject jsonInput = new JSONObject();
        if (params.length == 2) {
            jsonInput.put("dni", params[0]);
            jsonInput.put("clave", params[1]);
        } else if (params.length == 1) {
            jsonInput.put("refreshToken", params[0]);
        }

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            String jsonResponse = scanner.useDelimiter("\\A").next();
            JSONObject jsonObject = new JSONObject(jsonResponse);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.accessToken = jsonObject.getString("accessToken");
            tokenResponse.refreshToken = jsonObject.getString("refreshToken");
            return tokenResponse;
        }
    }
}
