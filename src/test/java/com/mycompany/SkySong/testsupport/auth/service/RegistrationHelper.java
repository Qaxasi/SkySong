package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.RegistrationService;
import com.mycompany.SkySong.shared.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;

public class RegistrationHelper {
    public static void newUserRegisterRequest(RegistrationService registrationService) throws DatabaseException {
        String username = "testUniqueUsername";
        String email = "testUniqueEmail@gmail.com";
        String password = "testPassword@123";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        registrationService.register(registerRequest);
    }
}
