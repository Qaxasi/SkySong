package com.mycompany.SkySong.identity.application.registration.ports;

public interface RegistrationUserRepository {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
