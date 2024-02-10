package com.mycompany.SkySong.auth.security;

public class UserAuthenticatorImpl implements UserAuthenticator {

    private final SessionUserInfoProvider userInfoProvider;
    private final CustomUserDetailsService userDetails;

    public UserAuthenticatorImpl(SessionUserInfoProvider userInfoProvider, CustomUserDetailsService userDetails) {
        this.userInfoProvider = userInfoProvider;
        this.userDetails = userDetails;
    }

    @Override
    public void authenticateUser(String sessionId) {

    }
}
