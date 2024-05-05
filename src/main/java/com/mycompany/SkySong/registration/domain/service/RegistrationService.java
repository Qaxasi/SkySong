package com.mycompany.SkySong.registration.domain.service;

class RegistrationService {

    private final UserRegistration registration;
    private final RequestValidation validation;

    RegistrationService(UserRegistration registration, RequestValidation validation) {
        this.registration = registration;
        this.validation = validation;
    }
}
