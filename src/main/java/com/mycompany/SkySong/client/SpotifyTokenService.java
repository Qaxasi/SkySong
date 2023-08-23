package com.mycompany.SkySong.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class SpotifyTokenService {

    private static final String SPOTIFY_CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String SPOTIFY_CLIENT_SECRET = System.getenv("SPOTIFY_CLIENT_SECRET");
    private static final String SPOTIFY_TOKEN_API_URL_TEMPLATE = "https://accounts.spotify.com/api/token";
    private final OkHttpClient client = new OkHttpClient();
    private String cachedToken;
    private Instant tokenExpiryTime;

    public String getAccessToken() throws Exception {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiryTime)) {
            return cachedToken;
        }

        RequestBody fromBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        String credentials = Credentials.basic(SPOTIFY_CLIENT_ID, SPOTIFY_CLIENT_SECRET);

        Request request = new Request.Builder()
                .url(SPOTIFY_TOKEN_API_URL_TEMPLATE)
                .post(fromBody)
                .header("Authorization", credentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to get access token. Http response code:" + response.code());
            }

            String responseData = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseData);

            cachedToken = jsonResponse.getString("access_token");

            tokenExpiryTime = Instant.now().plusSeconds(jsonResponse.getInt("expires_int") - 10);
            return cachedToken;
        }
    }
}
