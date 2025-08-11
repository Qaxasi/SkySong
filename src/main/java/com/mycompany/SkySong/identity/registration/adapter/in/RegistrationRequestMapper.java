package com.mycompany.SkySong.identity.registration.adapter.in;

import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationInput;

public class RegistrationRequestMapper {

    public UserRegistrationInput toDto(final RegistrationRequest request) {
        return new UserRegistrationInput(
                request.username(),
                request.email(),
                request.password());
    }
}
