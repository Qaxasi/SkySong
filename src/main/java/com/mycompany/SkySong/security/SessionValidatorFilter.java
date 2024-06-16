package com.mycompany.SkySong.security;

import com.mycompany.SkySong.infrastructure.persistence.dao.SessionDAO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
public class SessionValidatorFilter extends OncePerRequestFilter {

    private final CustomAuthenticationEntryPoint authEntryPoint;
    private final SessionExtractor sessionExtractor;
    private final SessionAuthentication authenticator;
    private final SessionDAO sessionDAO;

    public SessionValidatorFilter(CustomAuthenticationEntryPoint authEntryPoint,
                                  SessionExtractor sessionExtractor,
                                  SessionAuthentication authenticator,
                                  SessionDAO sessionDAO) {
        this.authEntryPoint = authEntryPoint;
        this.sessionExtractor = sessionExtractor;
        this.authenticator = authenticator;
        this.sessionDAO = sessionDAO;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {


        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String sessionId = sessionExtractor.getSessionIdFromRequest(request);

        if (isValidSession(sessionId)) {
            authenticator.authenticateUser(sessionId);
        } else {
            handleAuthenticationFailure(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getSessionIdFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "session_id".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private boolean isValidSession(String sessionId) {
       if (sessionId == null) {
           return false;
       }
        return sessionDAO.findById(sessionId)
                .map(session -> session.getExpiresAt().after(new Date()))
                .orElse(false);
    }

    private void handleAuthenticationFailure(HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        authEntryPoint.commence(request, response, new InsufficientAuthenticationException(
                "Access denied due to invalid or missing token."));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return  path.startsWith("/api/v1/users/login") ||
                path.startsWith("/api/v1/users/register");
    }
}
