package com.mycompany.SkySong.domain.user.registration.ports;

public interface PasswordEncoder {
    String encode(CharSequence  password);
}
