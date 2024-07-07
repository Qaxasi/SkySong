package com.mycompany.SkySong.registration.domain.ports;

public interface PasswordEncoder {
    String encode(CharSequence  password);
}
