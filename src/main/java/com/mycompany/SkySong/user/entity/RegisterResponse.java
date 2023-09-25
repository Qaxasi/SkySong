package com.mycompany.SkySong.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterResponse(@JsonProperty("usernameOrEmail") String usernameOrEmail,
                               @JsonProperty("password") String password) {
}
