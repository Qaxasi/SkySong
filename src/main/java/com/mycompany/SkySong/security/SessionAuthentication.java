package com.mycompany.SkySong.security;

import com.mycompany.SkySong.common.entity.Session;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.dao.SessionDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.security.exception.SessionNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionAuthentication {

    private final CustomUserDetailsService userDetails;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    public SessionAuthentication(CustomUserDetailsService userDetails,
                                 SessionDAO sessionDAO,
                                 UserDAO userDAO) {
        this.userDetails = userDetails;
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
    }
    
    public void authenticateUser(String sessionId) {
        String username = getUsernameForSession(sessionId);
        UserDetails details = userDetails.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getUsernameForSession(String sessionId) {
        return Optional.of(sessionId)
                .flatMap(sessionDAO::findById)
                .map(Session::getUserId)
                .flatMap(userDAO::findById)
                .map(User::getUsername)
                .orElseThrow(() -> new SessionNotFoundException("No user associated with session ID:" + sessionId));
    }
}
