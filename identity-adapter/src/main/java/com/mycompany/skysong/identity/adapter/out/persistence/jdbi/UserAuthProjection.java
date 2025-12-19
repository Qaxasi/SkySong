package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

public record UserAuthProjection(int userId, String username, String passwordHash) {
}
