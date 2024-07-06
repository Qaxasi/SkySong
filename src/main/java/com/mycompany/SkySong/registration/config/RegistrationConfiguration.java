package com.mycompany.SkySong.registration.config;

import com.mycompany.SkySong.registration.mapper.RoleMapper;
import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import com.mycompany.SkySong.registration.domain.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.registration.domain.ports.RegistrationUserRepository;
import com.mycompany.SkySong.registration.domain.ports.UserSaver;
import com.mycompany.SkySong.registration.domain.service.UserRegistration;
import com.mycompany.SkySong.registration.domain.service.UserRegistrationFactory;
import com.mycompany.SkySong.registration.mapper.UserRegistrationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegistrationConfiguration {
    @Bean
    UserRegistration registration(RegistrationUserRepository userRepository,
                                  RegistrationRoleRepository roleRepository,
                                  PasswordEncoder encoder,
                                  UserRegistrationMapper userMapper,
                                  UserSaver userSaver) {
        UserRegistrationFactory registrationFactory = new UserRegistrationFactory();
        return registrationFactory.createUserRegistration(userRepository, roleRepository, encoder, userMapper, userSaver);
    }
}
