package com.mycompany.skysong.identity.application.authentication.model;

import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.identity.domain.UserRole;

import java.util.Set;

public record AccessTokenClaims(UserId userId, Set<UserRole> roles) {
}
