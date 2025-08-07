package com.mycompany.SkySong.security.filter;

import com.mycompany.SkySong.infrastructure.context.UserContext;
import com.mycompany.SkySong.security.exception.TokenExpiredException;
import com.mycompany.SkySong.security.handler.CustomAuthenticationEntryPoint;
import com.mycompany.SkySong.security.jwt.JwtTokenVerifier;
import com.mycompany.SkySong.infrastructure.config.security.SecurityProperties;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenVerifier jwtTokenVerifier;
    private final CustomAuthenticationEntryPoint authEntryPoint;
    private final SecurityProperties securityProperties;

    public JwtAuthenticationFilter(JwtTokenVerifier jwtTokenVerifier,
                                   CustomAuthenticationEntryPoint authEntryPoint,
                                   SecurityProperties securityProperties) {
        this.jwtTokenVerifier = jwtTokenVerifier;
        this.authEntryPoint = authEntryPoint;
        this.securityProperties = securityProperties;
    }


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromCookies(request);
            if (jwt != null) {

                jwtTokenVerifier.validateToken(jwt);

                String username = jwtTokenVerifier.extractUsername(jwt);
                List<SimpleGrantedAuthority> authorities = jwtTokenVerifier.extractRoles(jwt).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                int id = jwtTokenVerifier.extractUserId(jwt);
                UserContext.setUserId(id);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            authEntryPoint.commence(request, response, new TokenExpiredException(
                    "Your session has expired. Please refresh your token to continue."));
        } finally {
            UserContext.clear();
        }
    }



    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwtToken".equals(cookie.getName()))
                    .findFirst();

            if (jwtCookie.isPresent()) {
                return jwtCookie.get().getValue();
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return securityProperties.getExcludePaths().stream().anyMatch(path::startsWith);
    }
}
