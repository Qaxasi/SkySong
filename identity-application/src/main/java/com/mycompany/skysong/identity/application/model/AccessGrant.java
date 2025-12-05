package com.mycompany.SkySong.identity.application.model;

import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.UserTag;

import java.time.Duration;

public record AccessGrant(AccessToken accessToken, long accessTokenExpiresInSec,
                          RefreshToken refreshToken, Duration sessionTtl,
                          UserTag userTag) {
}
