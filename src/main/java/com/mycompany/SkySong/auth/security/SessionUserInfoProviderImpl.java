package com.mycompany.SkySong.auth.security;

import org.springframework.stereotype.Service;

@Service
public class SessionUserInfoProviderImpl implements SessionUserInfoProvider {

    @Override
    public String getUsernameForSession(String sessionId) {
        return null;
    }
}
