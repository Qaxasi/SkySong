package com.mycompany.SkySong.authentication.secutiry;

import com.mycompany.SkySong.authentication.service.CookieService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProviderImpl jwtTokenProviderImpl;
    private final UserDetailsService userDetailsService;
    private final CookieService cookieService;

    public JwtAuthenticationFilter(JwtTokenProviderImpl jwtTokenProviderImpl, UserDetailsService userDetailsService, CookieService cookieService) {
        this.jwtTokenProviderImpl = jwtTokenProviderImpl;
        this.userDetailsService = userDetailsService;
        this.cookieService = cookieService;
    }

    @Override
    public void doFilterInternal(@NotNull HttpServletRequest request,
                                 @NotNull HttpServletResponse response,
                                 @NotNull FilterChain filterChain) throws ServletException, IOException {


        log.info("JwtFilter - processing request to {}", request.getRequestURI());

        String token = cookieService.getCookie(request, "auth_token")
                .map(Cookie::getValue)
                .orElse(null);

        if (StringUtils.hasText(token) && jwtTokenProviderImpl.validateToken(token)) {

            Claims claims = jwtTokenProviderImpl.getClaimsFromToken(token);

            String username = claims.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/users/login") || path.startsWith("/api/v1/users/register");
    }
}
