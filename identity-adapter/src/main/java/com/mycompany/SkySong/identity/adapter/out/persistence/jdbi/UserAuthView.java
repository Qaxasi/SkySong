package com.mycompany.SkySong.identity.a.adapter.out.persistence.jdbi;

public record UserAuthView(int userId, String username, String passwordHash, boolean enabled, boolean locked) {
}
