package com.mycompany.SkySong.registration.domain.ports;

public interface PasswordEncoderPort {
    String encode(CharSequence  password);
}
