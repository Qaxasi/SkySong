package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpotifyAccessTokenValidatorTest {

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

    @ParameterizedTest(name = "Invalid token response: {0}")
    @MethodSource("invalidSpotifyTokenResponses")
    void whenSpotifyTokenResponseInvalid_ValidationsFails(String caseDescription, SpotifyTokenResponse response) {
        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.UNPROCESSABLE_ENTITY);
    }

    private static Stream<Arguments> invalidSpotifyTokenResponses() {
        return Stream.of(
                Arguments.of("Empty access token", new SpotifyTokenResponse(" ", "refreshToken", "scope")),
                Arguments.of("Null access token", new SpotifyTokenResponse(null, "refreshToken", "scope")),
                Arguments.of("Empty refresh token", new SpotifyTokenResponse("accessToken", " ", "scope")),
                Arguments.of("Null refresh token", new SpotifyTokenResponse("accessToken", null, "scope")),
                Arguments.of("Empty scope", new SpotifyTokenResponse("accessToken", "refreshToken", " ")),
                Arguments.of("Null scope", new SpotifyTokenResponse("accessToken", "refreshToken", null))
        );
    }

    @Test
    void whenRequestIsValid_ValidationSuccess() {
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest("grant_type", "auth_code", "redirect_uri");

        Result<Void> result = validateRequest(request);

        assertThat(result.success()).isTrue();
    }

    @Test
    void whenGrantTypeIsNull_ValidationFailure() {
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(null, "authorization_code", "redirect_uri");

        Result<Void> result = validateRequest(request);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    private Result<Void> validateResponse(SpotifyTokenResponse response) {
        return validator.validateResponse(response);
    }

    private Result<Void> validateRequest(SpotifyAccessTokenRequest request) {
        return validator.validateRequest(request);
    }
}
