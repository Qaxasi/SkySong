package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpotifyAccessTokenValidatorTest {

    private SpotifyAccessTokenValidator validator;

    @BeforeEach
    void setup() {
        validator = new SpotifyAccessTokenValidator();
    }

    @Test
    void whenResponseValid_ValidationSuccess() {
        SpotifyTokenResponse response = new SpotifyTokenResponse("accessToken", "refreshToken", "scope");

        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isTrue();
    }

    @Test
    void whenAccessTokenIsEmpty_ValidationFails() {
        SpotifyTokenResponse response = new SpotifyTokenResponse("", "refreshToken", "scope");

        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.UNPROCESSABLE_ENTITY);
    }

    @Test
    void whenAccessTokenIsNull_ValidationFails() {
        SpotifyTokenResponse response = new SpotifyTokenResponse(null, "refreshToken", "scope");

        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.UNPROCESSABLE_ENTITY);
    }

    private Result<Void> validateResponse(SpotifyTokenResponse response) {
        return validator.validateResponse(response);
    }
}
