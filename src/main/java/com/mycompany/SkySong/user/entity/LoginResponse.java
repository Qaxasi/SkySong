package com.mycompany.SkySong.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(@JsonProperty("usernameOrEmail") String usernameOrEmail,
                            @JsonProperty("password") String password) {
}
