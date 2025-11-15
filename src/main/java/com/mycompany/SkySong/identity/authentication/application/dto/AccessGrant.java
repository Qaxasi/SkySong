package com.mycompany.SkySong.identity.authentication.application.dto;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.identity.authentication.domain.UserTag;

import java.time.Duration;

public record AccessGrant(AccessToken accessToken, long accessTokenExpiresInSec,
                          RefreshToken refreshToken, Duration sessionTtl,
                          UserTag userTag) {
}
