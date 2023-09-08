package com.mycompany.SkySong.music.controller;

import com.mycompany.SkySong.music.authorization.controller.AuthorizationController;
import com.mycompany.SkySong.music.authorization.entity.SpotifyAccessToken;
import com.mycompany.SkySong.music.authorization.exception.AuthorizationException;
import com.mycompany.SkySong.music.authorization.service.SpotifyAuthorizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SpotifyAuthorizationService spotifyAuthorizationService;


    @Test
    void shouldReturnAuthorizationExceptionDuringLogin() throws Exception {
        when(spotifyAuthorizationService.getAuthorizationCodeURL()).thenThrow(
                new AuthorizationException("Authorization Exception"));
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isUnauthorized());
        verify(spotifyAuthorizationService).getAuthorizationCodeURL();
    }
    @Test
    void shouldReturnRedirectViewAndStatusRedirectionWithAuthorizationCodeURLFetchedFromService() throws Exception {
        String authorizationCodeURL = "https://accounts.spotify.com/authorize?someParams";
        when(spotifyAuthorizationService.getAuthorizationCodeURL()).thenReturn(authorizationCodeURL);
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(redirectedUrl(authorizationCodeURL))
                .andExpect(status().is3xxRedirection());
        verify(spotifyAuthorizationService).getAuthorizationCodeURL();
    }

    @Test
    void shouldReturnValidTokenWhenAuthCodeProvided() throws Exception {
        final var authorizationCode = "test-code";
        final SpotifyAccessToken expectedAccessToken = new SpotifyAccessToken(
                "test-token",
                "refresh-test-token",
                3600,
                "test-scope");
        when(spotifyAuthorizationService.fetchAccessToken(anyString())).thenReturn(expectedAccessToken);
        final String expectedBody = "{" +
                "\"access_token\":\"test-token\"," +
                "\"refresh_token\":\"refresh-test-token\"," +
                "\"expires_in\":3600," +
                "\"scope\":\"test-scope\"" +
                "}";
        mockMvc.perform(
                        get("/api/auth/token")
                                .param("code", authorizationCode)
                )
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedBody))
                .andExpect(status().is2xxSuccessful());
        verify(spotifyAuthorizationService).fetchAccessToken(authorizationCode);
    }
}




