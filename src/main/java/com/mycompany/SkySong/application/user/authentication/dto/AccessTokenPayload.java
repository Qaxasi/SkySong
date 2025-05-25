package com.mycompany.SkySong.application.user.authentication.dto;

import java.util.List;

public interface AccessTokenPayload {
    int id();
    String usernameOrEmail();
    List<String> roles();
}
