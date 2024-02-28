package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthProcessorImpl implements UserAuthProcessor {

    private final UserAuthentication userAuth;
    private final UserRetrieval userRetrieval;

    public UserAuthProcessorImpl(UserAuthentication userAuth, UserRetrieval userRetrieval) {
        this.userAuth = userAuth;
        this.userRetrieval = userRetrieval;
    }

    @Override
    public User fetchUserByAuthentication(LoginRequest request) {
        Authentication auth = userAuth.authenticateUser(request);
        return userRetrieval.findByAuthUsername(auth.getName());
    }
}
