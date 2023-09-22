package com.mycompany.SkySong.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterResponse(@JsonProperty("username") String username,
                               @JsonProperty("email") String email,
                               @JsonProperty("password") String password) {
}
