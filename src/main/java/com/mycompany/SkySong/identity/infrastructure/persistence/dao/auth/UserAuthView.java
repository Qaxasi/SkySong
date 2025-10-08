package com.mycompany.SkySong.identity.infrastructure.persistence.dao.auth;

public record UserAuthView(int userId, String username, String passwordHash, boolean enabled, boolean locked) {
}
