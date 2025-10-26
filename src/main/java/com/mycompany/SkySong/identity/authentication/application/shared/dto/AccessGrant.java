package com.mycompany.SkySong.identity.authentication.application.shared.dto;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;

import java.time.Duration;

public record AccessGrant(AccessToken accessToken, RefreshToken refreshToken, Duration refreshTokenTtl) {
}
