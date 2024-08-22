package com.mycompany.SkySong.adapter.security.filter;

import com.mycompany.SkySong.adapter.login.exception.TokenExpiredException;
import com.mycompany.SkySong.adapter.security.CustomAuthenticationEntryPoint;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
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

    private final JwtTokenManager jwtManager;
    private final CustomAuthenticationEntryPoint authEntryPoint;

    public JwtAuthenticationFilter(JwtTokenManager jwtManager,
                                   CustomAuthenticationEntryPoint authEntryPoint) {
        this.jwtManager = jwtManager;
        this.authEntryPoint = authEntryPoint;
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
            if (jwt != null && (jwtManager.isTokenValid(jwt))) {
                String username = jwtManager.extractUsername(jwt);
                List<String> roles = jwtManager.extractRoles(jwt);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

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
}
