package com.mycompany.SkySong.application.user.login.dto;

import java.util.List;

public record AuthenticatedUser(int id, String usernameOrEmail, List<String> roles) {
}
