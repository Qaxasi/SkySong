package com.mycompany.SkySong.login.domain.ports;

public interface UserAuthentication {
    String authenticateUser(String usernameOrEmail, String password);
}
