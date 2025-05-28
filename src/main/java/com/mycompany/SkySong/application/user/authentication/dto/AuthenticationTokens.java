package com.mycompany.SkySong.application.user.authentication.login.dto;

import com.mycompany.SkySong.application.user.authentication.dto.AccessToken;
import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;

public record AuthenticationTokens(AccessToken accessToken, RefreshToken refreshToken) {
}
