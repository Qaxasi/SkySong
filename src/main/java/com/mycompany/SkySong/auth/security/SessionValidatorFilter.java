package com.mycompany.SkySong.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SessionValidatorFilter extends OncePerRequestFilter {

    private final SessionValidation session;
    private final CustomUserDetailsService userDetails;
    private final SessionUserInfoProvider userInfoProvider;

    public SessionValidatorFilter(SessionValidation session,
                                  CustomUserDetailsService userDetails,
                                  SessionUserInfoProvider userInfoProvider) {
        this.session = session;
        this.userDetails = userDetails;
        this.userInfoProvider = userInfoProvider;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {


        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String sessionId = Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "session_id".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);


        if (sessionId != null && session.validateSession(sessionId)) {

            String username = userInfoProvider.getUsernameForSession(sessionId);
            UserDetails details = userDetails.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return  path.startsWith("/api/v1/users/login") ||
                path.startsWith("/api/v1/users/register");
    }
}
