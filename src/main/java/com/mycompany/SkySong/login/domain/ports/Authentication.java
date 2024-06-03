package com.mycompany.SkySong.login.domain.ports;

public interface Authentication {
    String authenticateUser(String usernameOrEmail, String password);
}
