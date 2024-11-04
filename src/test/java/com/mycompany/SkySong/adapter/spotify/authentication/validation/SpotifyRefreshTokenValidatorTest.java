package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.shared.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpotifyRefreshTokenValidatorTest {

    private SpotifyRefreshTokenValidator validator;

    @BeforeEach
    void setup() {
        validator = new SpotifyRefreshTokenValidator();
    }

    @Test
    void whenValidRequest_ValidationSuccess() {
        SpotifyRefreshTokenRequest request = new SpotifyRefreshTokenRequest("grant_type", "refresh_token");

        Result<Void> result = validateRequest(request);

        assertThat(result.success()).isTrue();
    }

    private Result<Void> validateRequest(SpotifyRefreshTokenRequest request) {
        return validator.validateRequest(request);
    }
}
