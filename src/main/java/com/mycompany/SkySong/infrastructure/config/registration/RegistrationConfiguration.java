package com.mycompany.SkySong.infrastructure.config.registration;

import com.mycompany.SkySong.application.registration.mapper.RoleMapper;
import com.mycompany.SkySong.application.registration.mapper.UserRegistrationMapper;
import com.mycompany.SkySong.application.registration.usecase.UserRegistrationHandler;
import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.registration.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.domain.registration.ports.RegistrationUserRepository;
import com.mycompany.SkySong.domain.registration.ports.UserSaver;
import com.mycompany.SkySong.domain.registration.service.UserRegistrationValidator;
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
    public UserRegistrationHandler registrationHandler(UserRegistrationValidator validation,
                                                       UserCreator userCreator,
                                                       UserSaver userSaver,
                                                       UserRegistrationMapper userMapper) {
        return new UserRegistrationHandler(validation, userCreator, userSaver, userMapper);
    }

    @Bean
    public UserRegistrationValidator requestValidation(RegistrationUserRepository userRepository) {
        return new UserRegistrationValidator(userRepository);
    }
    @Bean
    public UserCreator userCreator(PasswordEncoder encoder, RegistrationRoleRepository roleRepository) {
        return new UserCreator(encoder, roleRepository);
    }
}
