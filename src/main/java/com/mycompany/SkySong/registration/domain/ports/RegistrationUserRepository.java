package com.mycompany.SkySong.registration.domain.ports;

public interface RegistrationUserRepository {
    boolean existsByUsername(String username);
    boolean existsByEmail( String email);
}
