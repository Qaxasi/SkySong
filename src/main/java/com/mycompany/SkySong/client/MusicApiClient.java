package com.mycompany.SkySong.client;

import okhttp3.*;
import org.json.JSONObject;

public class MusicApiClient {

    private static final String SPOTIFY_CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String SPOTIFY_CLIENT_SECRET = System.getenv("SPOTIFY_CLIENT_SECRET");
    private static final String SPOTIFY_TOKEN_API_URL_TEMPLATE = "https://accounts.spotify.com/api/token";

    public static String getAccessToken() throws Exception {
        OkHttpClient client = new OkHttpClient();

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
            return jsonResponse.getString("access_token");
        }
    }
}
