package com.mycompany.SkySong.infrastructure.config.registration;

import com.mycompany.SkySong.application.registration.mapper.RoleMapper;
import com.mycompany.SkySong.application.registration.mapper.UserRegistrationMapper;
import com.mycompany.SkySong.application.registration.usecase.UserRegistrationHandler;
import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.registration.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.domain.registration.ports.RegistrationUserRepository;
import com.mycompany.SkySong.domain.registration.ports.UserSaver;
import com.mycompany.SkySong.domain.registration.service.RequestValidation;
import com.mycompany.SkySong.domain.registration.service.UserCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistrationConfiguration {

    @Bean
    public RoleMapper roleMapper() {
        return new RoleMapper();
    }

    @Bean
    public UserRegistrationMapper userRegistrationMapper(RoleMapper roleMapper) {
        return new UserRegistrationMapper(roleMapper);
    }
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
