package com.mycompany.SkySong.auth.security;

public interface UserAuthenticator {

    void authenticateUser(String sessionId);
}
