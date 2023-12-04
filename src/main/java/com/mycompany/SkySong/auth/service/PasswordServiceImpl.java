package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationMessageService messageService;

    public PasswordServiceImpl(PasswordEncoder passwordEncoder, ApplicationMessageService messageService) {
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }
    @Override
    public String encodePassword(String password) {
        try {
            return passwordEncoder.encode(password);
        } catch (IllegalArgumentException ex) {
            log.error("Error encoding the password: " + ex.getMessage(), ex);
            throw new InternalErrorException(messageService.getMessage("password.encoding.error"));
        }
    }
}
