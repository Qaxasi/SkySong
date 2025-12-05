package com.mycompany.SkySong.identity.adapter.out.persistence.jdbi;

public record UserAuthView(int userId, String username, String passwordHash) {
}
