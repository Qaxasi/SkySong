package com.mycompany.SkySong.identity.application.registration.ports;

public interface PasswordEncoder {
    String encode(CharSequence  password);
}
