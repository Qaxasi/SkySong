package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
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

    @ParameterizedTest(name = "Invalid token request: {0}")
    @MethodSource("invalidRefreshTokenRequests")
    void whenInvalidRequest_ValidationFailure(String caseDescription, SpotifyRefreshTokenRequest request) {
        Result<Void> result = validateRequest(request);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    void whenResponseWithoutRefreshToken_ValidationSuccess() {
        SpotifyTokenResponse response = new SpotifyTokenResponse("access_token", null, "scope");

        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isTrue();
    }

    @ParameterizedTest(name = "Invalid token response: {0}")
    @MethodSource("invalidRefreshTokenResponses")
    void whenInvalidResponse_ValidationFailure(String caseDescription, SpotifyTokenResponse response) {
        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.UNPROCESSABLE_ENTITY);
    }

    private static Stream<Arguments> invalidRefreshTokenRequests() {
        return Stream.of(
                Arguments.of("Null grant type", new SpotifyRefreshTokenRequest(null, "refresh_token")),
                Arguments.of("Empty grant type", new SpotifyRefreshTokenRequest(" ", "refresh_token")),
                Arguments.of("Null refresh token", new SpotifyRefreshTokenRequest("grant_type", null)),
                Arguments.of("Empty refresh token", new SpotifyRefreshTokenRequest("grant_type", " "))
        );
    }

    private static Stream<Arguments> invalidRefreshTokenResponses() {
        return Stream.of(
                Arguments.of("Null access token", new SpotifyTokenResponse(null, "refresh_token", "scope")),
                Arguments.of("Empty access token", new SpotifyTokenResponse(" ", "refresh_token", "scope")),
                Arguments.of("Null scope", new SpotifyTokenResponse("access_token", "refresh_token", null)),
                Arguments.of("Empty scope", new SpotifyTokenResponse("access_token", "refresh_token", " "))
        );
    }


    private Result<Void> validateRequest(SpotifyRefreshTokenRequest request) {
        return validator.validateRequest(request);
    }

    private Result<Void> validateResponse(SpotifyTokenResponse response) {
        return validator.validateResponse(response);
    }
}
