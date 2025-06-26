package com.mycompany.SkySong.identity.application.authentication.dto;

import java.util.List;

public interface AccessTokenPayload {
    int id();
    String username();
    List<String> roles();
}
