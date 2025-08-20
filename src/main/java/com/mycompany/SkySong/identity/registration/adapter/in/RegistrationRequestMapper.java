package com.mycompany.SkySong.identity.registration.adapter.in;

import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationData;

import java.util.Locale;

public class RegistrationRequestMapper {

    public UserRegistrationData toDto(final RegistrationRequest request) {
        return new UserRegistrationData(
                request.username().strip(),
                request.email().strip().toLowerCase(Locale.ROOT),
                request.password());
    }
}
