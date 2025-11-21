package com.mycompany.SkySong.infrastructure.security.filter;

import com.mycompany.SkySong.infrastructure.InvalidTokenException;
import com.mycompany.SkySong.infrastructure.context.UserContext;
import com.mycompany.SkySong.infrastructure.security.exception.TokenExpiredException;
import com.mycompany.SkySong.infrastructure.security.handler.CustomAuthenticationEntryPoint;
import com.mycompany.SkySong.infrastructure.security.jwt.JwtPrincipal;
import com.mycompany.SkySong.infrastructure.security.jwt.JwtTokenVerifier;
import com.mycompany.SkySong.identity.authentication.adapter.in.web.security.security.SecurityProperties;
import com.mycompany.SkySong.infrastructure.security.jwt.VerifiedJwt;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;;
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
import java.util.OptionalInt;

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
            final Optional<String> jwtOpt = getJwtFromCookies(request);
            if (jwtOpt.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = jwtOpt.get();
            final Result<VerifiedJwt> verificationResult = jwtTokenVerifier.verify(jwt);
            if (verificationResult.isFailure()) {
                if (verificationResult.errorType() == ErrorType.EXPIRED_JWT_TOKEN) {
                    SecurityContextHolder.clearContext();
                    authEntryPoint.commence(request, response,
                            new TokenExpiredException("Your session has expired. Please refresh your token to continue."));
                } else {
                    SecurityContextHolder.clearContext();
                    authEntryPoint.commence(request, response,
                            new InvalidTokenException("Invalid token."));
                }
                return;
            }

            final VerifiedJwt verifiedJwt = verificationResult.get();

            final OptionalInt userIdOpt = verifiedJwt.userId();
            if (userIdOpt.isEmpty()) {
                SecurityContextHolder.clearContext();
                authEntryPoint.commence(request, response,
                        new InvalidTokenException("Missing or invalid user id."));
                return;
            }


            final int userId = userIdOpt.getAsInt();
            final String username = verifiedJwt.username();
            final List<String> roles = verifiedJwt.roles();
            final long sessionVersion = verifiedJwt.sessionVersion();

            final JwtPrincipal principal = new JwtPrincipal(userId, username, roles, sessionVersion);

            final List<SimpleGrantedAuthority> authorities = verifiedJwt.roles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UserContext.setUserId(userId);

            final UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } finally {
            UserContext.clear();
        }
    }

    private Optional<String> getJwtFromCookies(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(c -> "jwtToken".equals(c.getName()))
                .map(Cookie::getValue)
                .filter(v -> v != null && !v.isBlank())
                .findFirst();
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        String path = request.getRequestURI();
        return securityProperties.getExcludePaths().stream().anyMatch(path::startsWith);
    }
}
