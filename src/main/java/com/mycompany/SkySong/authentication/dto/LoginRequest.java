package com.mycompany.SkySong.dto;

public record LoginRequest(
        String usernameOrEmail,
        String password) {
}
