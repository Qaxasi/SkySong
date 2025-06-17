package com.mycompany.SkySong.identity.application.registration.ports;

public interface PasswordHasher {
    String hash(CharSequence  password);
}
