package com.mycompany.SkySong.identity.authentication.application.shared.dto;

import com.mycompany.SkySong.identity.shared.domain.UserId;
import com.mycompany.SkySong.identity.shared.domain.UserRole;

import java.util.Set;

public record AccessTokenClaims(UserId userId, Set<UserRole> roles, long authzVersion) {
}
