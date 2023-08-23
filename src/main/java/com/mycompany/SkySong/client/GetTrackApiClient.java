package com.mycompany.SkySong.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GetTrackApiClient {
    private static final String SPOTIFY_GET_TRACK_URL = "https://api.spotify.com/v1/tracks/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private SpotifyTokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(GetTrackApiClient.class);

    public GetTrackApiClient(SpotifyTokenService tokenService) {
        this.tokenService = tokenService;
    }

    public JsonNode getTrackInfo(String trackId) throws Exception {
        String token;
        try {
            token = tokenService.getAccessToken();
        } catch (Exception ex) {
            logger.error("Error getting Spotify token", ex);
            throw new SpotifyApiException("Failed to fetch the Spotify access token.", ex);
        }
        Request request = new Request.Builder()
                .url(SPOTIFY_GET_TRACK_URL + trackId)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.body().string());
                if (jsonNode.has("error")) {
                    logger.error("API returned error: {}", jsonNode.get("error").asText());
                    throw new SpotifyApiException("API returned error: " + jsonNode.get("error").asText());
                }
                return jsonNode;
            } else {
                logger.error("Failed to fetch the track data. Response code: {}", response.code());
                throw new SpotifyApiException("Failed to fetch the track data. Response code: " + response.code());
            }
        } catch (IOException ex) {
            logger.error("Error processing API response", ex);
            throw new SpotifyApiException("Error processing API response", ex);
        }
    }
}
