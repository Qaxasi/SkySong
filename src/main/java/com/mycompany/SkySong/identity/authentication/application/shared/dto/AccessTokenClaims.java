package com.mycompany.SkySong.identity.authentication.application.shared.dto;

import java.util.Set;

public record AccessTokenClaims(int userId, String username, Set<String> roles, long sessionVersion) {
}
