package com.mycompany.SkySong.identity.authentication.login.application.dto;

import com.mycompany.SkySong.identity.authentication.shared.dto.AccessTokenPayload;

import java.util.List;

public record AuthenticatedUser(int userId, String username, List<String> roles) implements AccessTokenPayload {
}
