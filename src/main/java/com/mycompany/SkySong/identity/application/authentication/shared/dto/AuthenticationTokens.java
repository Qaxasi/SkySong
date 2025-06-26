package com.mycompany.SkySong.identity.application.authentication.dto;

import com.mycompany.SkySong.identity.domain.AccessToken;
import com.mycompany.SkySong.identity.domain.RefreshToken;

public record AuthenticationTokens(AccessToken accessToken, RefreshToken refreshToken) {
}
