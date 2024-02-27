package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserAuthProcessorImpl implements UserAuthProcessor {

    @Override
    public User authenticateAndRetrieveUser(LoginRequest request) {
        return null;
    }
}
