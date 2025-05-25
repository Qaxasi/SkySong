package com.mycompany.SkySong.application.user.authentication.login.dto;

import com.mycompany.SkySong.application.user.authentication.dto.AccessTokenPayload;

import java.util.List;

public record AuthenticatedUser(int id, String usernameOrEmail, List<String> roles) implements AccessTokenPayload {
}
