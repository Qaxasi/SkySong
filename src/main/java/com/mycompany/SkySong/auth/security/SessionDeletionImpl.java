package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class SessionDeletionImpl implements SessionDeletion {

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private SecureTokenGenerator tokenGenerator;

    @Override
    public void deleteSession(String sessionId) {

    }
}
