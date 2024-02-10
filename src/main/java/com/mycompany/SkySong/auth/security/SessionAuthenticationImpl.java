package com.mycompany.SkySong.auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SessionAuthenticationImpl implements SessionAuthentication {

    private final SessionUserInfoProvider userInfoProvider;
    private final CustomUserDetailsService userDetails;

    public SessionAuthenticationImpl(SessionUserInfoProvider userInfoProvider, CustomUserDetailsService userDetails) {
        this.userInfoProvider = userInfoProvider;
        this.userDetails = userDetails;
    }

    @Override
    public void authenticateUser(String sessionId) {
        String username = userInfoProvider.getUsernameForSession(sessionId);
        UserDetails details = userDetails.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
