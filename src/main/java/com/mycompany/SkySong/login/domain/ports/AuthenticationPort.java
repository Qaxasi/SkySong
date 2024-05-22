package com.mycompany.SkySong.login.domain.ports;

public interface AuthenticationPort {
    String authenticateUser(String usernameOrEmail, String password);
}
