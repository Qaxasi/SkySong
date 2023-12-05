package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
class UserFactoryImpl implements UserFactory {
    private final ApplicationMessageService messageService;
    public UserFactoryImpl(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }
    @Override
    public User createUser(RegisterRequest registerRequest, Role role) {
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(encodePassword(registerRequest.password()));
        user.setRoles(Collections.singleton(role));
        return user;
    }
}
