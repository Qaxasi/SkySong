package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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
    private final UserDetailsService userDetails;
    private final JwtAuthenticationEntryPoint authEntryPoint;
    private final ApplicationMessageService message;
    private final ClaimsExtractor extractor;
    private final TokenValidator validator;

    public JwtAuthenticationFilter(UserDetailsService userDetails,
                                   JwtAuthenticationEntryPoint authEntryPoint,
                                   ApplicationMessageService message,
                                   ClaimsExtractor extractor,
                                   TokenValidator validator) {
        this.userDetails = userDetails;
        this.authEntryPoint = authEntryPoint;
        this.message = message;
        this.extractor = extractor;
        this.validator = validator;
    }

    @Override
    public void doFilterInternal(@NotNull HttpServletRequest request,
                                 @NotNull HttpServletResponse response,
                                 @NotNull FilterChain filterChain) throws ServletException, IOException {


        log.info("JwtFilter - processing request to {}", request.getRequestURI());

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);

        if (isValidToken(token)) {
            authenticateUser(request, token);
            filterChain.doFilter(request, response);
        } else {
            handleInvalidToken(request, response);
        }
    }
    private void handleInvalidToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authEntryPoint.commence(request, response, new InsufficientAuthenticationException(
                message.getMessage("unauthorized.token.invalid")));
    }
    private void authenticateUser(HttpServletRequest request, String token) {
        Claims claims = extractor.getClaimsFromToken(token);

        String username = claims.getSubject();

        UserDetails userDetails = this.userDetails.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    private String extractToken(HttpServletRequest request) {
        return cookieRetriever.getCookie(request, "auth_token")
                .map(Cookie::getValue)
                .orElse(null);
    }
    private boolean isValidToken(String token) {
        return StringUtils.hasText(token) && validator.validateToken(token);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return  path.startsWith("/api/v1/users/login") ||
                path.startsWith("/api/v1/users/register");
    }
}
