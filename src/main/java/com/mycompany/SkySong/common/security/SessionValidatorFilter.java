package com.mycompany.SkySong.common.security;

import com.mycompany.SkySong.common.security.CustomAuthenticationEntryPoint;
import com.mycompany.SkySong.common.security.SessionAuthentication;
import com.mycompany.SkySong.common.security.SessionExtractor;
import com.mycompany.SkySong.common.security.SessionValidation;
import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SessionValidatorFilter extends OncePerRequestFilter {

    private final SessionValidation session;
    private final CustomAuthenticationEntryPoint authEntryPoint;
    private final ApplicationMessageLoader message;
    private final SessionExtractor sessionExtractor;
    private final SessionAuthentication authenticator;

    public SessionValidatorFilter(SessionValidation session,
                                  CustomAuthenticationEntryPoint authEntryPoint,
                                  ApplicationMessageLoader message,
                                  SessionExtractor sessionExtractor,
                                  SessionAuthentication authenticator) {
        this.session = session;
        this.authEntryPoint = authEntryPoint;
        this.message = message;
        this.sessionExtractor = sessionExtractor;
        this.authenticator = authenticator;
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

    private boolean isValidSession(String sessionId) {
        return sessionId != null && session.validateSession(sessionId);
    }

    private void handleAuthenticationFailure(HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        authEntryPoint.commence(request, response, new InsufficientAuthenticationException(
                message.getMessage("unauthorized.token.invalid")));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return  path.startsWith("/api/v1/users/login") ||
                path.startsWith("/api/v1/users/register");
    }
}
