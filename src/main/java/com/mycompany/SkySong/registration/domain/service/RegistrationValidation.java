package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.domain.ports.UserRepositoryPort;

class RegistrationValidation {

    private final UserRepositoryPort userRepository;

    RegistrationValidation(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }
}
