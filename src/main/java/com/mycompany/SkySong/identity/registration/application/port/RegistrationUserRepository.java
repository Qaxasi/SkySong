package com.mycompany.SkySong.identity.registration.application.port;

public interface RegistrationUserRepository {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
