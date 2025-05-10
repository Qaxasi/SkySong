package com.mycompany.SkySong.application.user.token.refresh.dto;

import java.io.Serializable;
import java.util.List;

public record SessionUser(int id, String usernameOrEmail, List<String> roles) implements Serializable {
}
