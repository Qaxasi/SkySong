package com.mycompany.skysong.identity.application.user.authentication.model;

import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.identity.domain.UserRole;
import com.mycompany.skysong.identity.domain.UserTag;

import java.util.Set;

public record UserAccessSnapshot(
        UserTag userTag,
        Set<UserRole> roles) {
    public AccessTokenClaims toClaims(final UserId userId) {
        return new AccessTokenClaims(userId, roles);
    }
}
