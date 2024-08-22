package com.mycompany.SkySong.adapter.security.filter;

import com.mycompany.SkySong.adapter.security.CustomAuthenticationEntryPoint;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
    }
}
