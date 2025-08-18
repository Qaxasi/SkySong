package com.mycompany.SkySong.identity.authentication.application.shared.dto;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;

public record AuthenticationTokens(AccessToken accessToken, RefreshToken refreshToken) {
}
