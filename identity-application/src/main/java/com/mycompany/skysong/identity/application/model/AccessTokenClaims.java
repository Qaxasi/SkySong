package com.mycompany.SkySong.identity.application.model;

import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.identity.domain.UserRole;

import java.util.Set;

public record AccessTokenClaims(UserId userId, Set<UserRole> roles) {
}
