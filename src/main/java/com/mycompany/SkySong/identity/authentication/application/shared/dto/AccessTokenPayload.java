package com.mycompany.SkySong.identity.authentication.shared.dto;

import java.util.List;

public interface AccessTokenPayload {
    int userId();
    String username();
    List<String> roles();
}
