package com.mycompany.SkySong.domain.login.ports;

public interface UserAuthentication {
    String authenticateUser(String usernameOrEmail, String password);
}
