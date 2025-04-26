package com.mycompany.SkySong.domain.registration.ports;

public interface PasswordEncoder {
    String encode(CharSequence  password);
}
