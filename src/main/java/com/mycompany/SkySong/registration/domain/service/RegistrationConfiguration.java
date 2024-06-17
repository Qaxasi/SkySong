package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import com.mycompany.SkySong.registration.domain.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.registration.domain.ports.RegistrationUserRepository;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegistrationConfiguration {

    @Bean
    UserRegistration registration(PasswordEncoder encoder,
                                  RegistrationRoleRepository roleRepository,
                                  RegistrationUserRepository userRepository,
                                  UserSaver userSaver) {
        RequestValidation validation = new RequestValidation(userRepository);
        UserCreator userCreator = new UserCreator(encoder, roleRepository);
        return new UserRegistration(validation, userCreator, userSaver);
    }
}
