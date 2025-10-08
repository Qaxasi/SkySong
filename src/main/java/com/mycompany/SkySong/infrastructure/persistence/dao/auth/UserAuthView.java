package com.mycompany.SkySong.infrastructure.persistence.dao.auth;

public record UserAuthView(int userId, String username, String passwordHash, boolean enabled, boolean locked) {
}
