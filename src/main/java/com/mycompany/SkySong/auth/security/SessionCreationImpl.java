package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionCreationImpl implements SessionCreation {

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private SecureTokenGenerator tokenGenerator;

    @Override
    public String createSession(Integer userID) {
        return null;
    }
}
