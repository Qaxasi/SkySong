package com.mycompany.SkySong.domain.user.registration.ports;

public interface RegistrationUserRepository {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
