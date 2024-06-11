package com.mycompany.SkySong.registration.domain.ports;

public interface UserRepository {
    boolean existsByUsername(String username);
    boolean existsByEmail( String email);
}
