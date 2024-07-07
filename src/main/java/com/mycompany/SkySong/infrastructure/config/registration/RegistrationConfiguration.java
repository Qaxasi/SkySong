package com.mycompany.SkySong.infrastructure.registration.config;

import com.mycompany.SkySong.common.mapper.RoleMapper;
import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.registration.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.domain.registration.ports.RegistrationUserRepository;
import com.mycompany.SkySong.domain.registration.ports.UserSaver;
import com.mycompany.SkySong.domain.registration.service.UserRegistration;
import com.mycompany.SkySong.domain.registration.service.UserRegistrationFactory;
import com.mycompany.SkySong.common.mapper.UserRegistrationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegistrationConfiguration {

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
