package com.mycompany.SkySong.adapter.spotify.authentication.token.access.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.validation.SpotifyAccessTokenValidator;
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
    void whenValidTokenResponse_ValidationSuccess() {
        SpotifyTokenResponse response = new SpotifyTokenResponse("accessToken", "refreshToken");

        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isTrue();
    }

    @ParameterizedTest(name = "Invalid token response: {0}")
    @MethodSource("invalidSpotifyTokenResponses")
    void whenInvalidTokenResponse_ValidationFailure(String caseDescription, SpotifyTokenResponse response) {
        Result<Void> result = validateResponse(response);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.UNPROCESSABLE_ENTITY);
    }

    @Test
    void whenValidTokenRequest_ValidationSuccess() {
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest("grant_type", "auth_code", "redirect_uri");

        Result<Void> result = validateRequest(request);

        assertThat(result.success()).isTrue();
    }

    @ParameterizedTest(name = "Invalid token request: {0}")
    @MethodSource("invalidSpotifyTokenRequests")
    void whenInvalidTokenRequest_ValidationFailure(String caseDescription, SpotifyAccessTokenRequest request) {
        Result<Void> result = validateRequest(request);

        assertThat(result.success()).isFalse();
        assertThat(result.errorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    private static Stream<Arguments> invalidSpotifyTokenResponses() {
        return Stream.of(
                Arguments.of("Empty access token", new SpotifyTokenResponse(" ", "refreshToken")),
                Arguments.of("Null access token", new SpotifyTokenResponse(null, "refreshToken")),
                Arguments.of("Empty refresh token", new SpotifyTokenResponse("accessToken", " ")),
                Arguments.of("Null refresh token", new SpotifyTokenResponse("accessToken", null))
        );
    }

    private static Stream<Arguments> invalidSpotifyTokenRequests() {
        return Stream.of(
                Arguments.of("Empty grant type", new SpotifyAccessTokenRequest(" ", "authorization_code", "redirect_uri")),
                Arguments.of("Null grant type", new SpotifyAccessTokenRequest(null, "authorization_code", "redirect_uri")),
                Arguments.of("Empty authorization code", new SpotifyAccessTokenRequest("grant_type", " ", "redirect_uri")),
                Arguments.of("Null authorization code", new SpotifyAccessTokenRequest("grant_type", null, "redirect_uri")),
                Arguments.of("Empty redirect uri", new SpotifyAccessTokenRequest("grant_type", "authorization_code", " ")),
                Arguments.of("Null redirect uri", new SpotifyAccessTokenRequest("grant_type", "authorization_code", null))
        );
    }

    private Result<Void> validateResponse(SpotifyTokenResponse response) {
        return validator.validateResponse(response);
    }

    private Result<Void> validateRequest(SpotifyAccessTokenRequest request) {
        return validator.validateRequest(request);
    }
}
