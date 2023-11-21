package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.ServiceFailureException;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.service.UserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class UserFactoryImpl implements UserFactory {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationMessageService messageService;
    public UserFactoryImpl(PasswordEncoder passwordEncoder, ApplicationMessageService messageService) {
        this.passwordEncoder = passwordEncoder;
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
    private String encodePassword(String password) {
        try {
            return passwordEncoder.encode(password);
        } catch (Exception ex) {
            log.error("Error encoding the password");
            throw new ServiceFailureException(messageService.getMessage("password.encoding.error"));

        }
    }
}
