package com.mycompany.SkySong.identity.authentication.application.shared.dto;

import com.mycompany.SkySong.identity.authentication.domain.AccessVersion;
import com.mycompany.SkySong.identity.authentication.domain.UserId;
import com.mycompany.SkySong.identity.authentication.domain.UserRole;

import java.util.Set;

public record AccessTokenClaims(UserId userId, Set<UserRole> roles, AccessVersion accessVersion) {
}
