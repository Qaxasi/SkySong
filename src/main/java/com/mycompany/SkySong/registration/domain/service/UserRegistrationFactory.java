package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import com.mycompany.SkySong.registration.domain.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.registration.domain.ports.RegistrationUserRepository;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;


public class UserRegistrationFactory {

    public UserRegistration createUserRegistration(RegistrationUserRepository userRepository,
                                                          RegistrationRoleRepository roleRepository,
                                                          PasswordEncoder encoder,
                                                          UserSaver userSaver)  {
        RequestValidation validation = new RequestValidation(userRepository);
        UserCreator userCreator = new UserCreator(encoder, roleRepository);
        return new UserRegistration(validation, userCreator, userSaver);
    }
}
