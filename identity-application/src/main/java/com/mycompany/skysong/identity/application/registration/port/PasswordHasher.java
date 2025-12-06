package com.mycompany.SkySong.identity.registration.application.port;

public interface PasswordHasher {
    String hash(CharSequence password);
}
