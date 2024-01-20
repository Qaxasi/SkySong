package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static com.mycompany.SkySong.testsupport.auth.security.JwtAuthenticationFilterTestHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @InjectMocks
    private JwtAuthenticationFilter authFilter;
    @Mock
    private CookieRetriever cookieRetriever;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private CustomUserDetailsService userDetails;
    @Mock
    private MockHttpServletResponse response;
    @Mock
    private MockHttpServletRequest request;
    @Mock
    private JwtAuthenticationEntryPoint authEntryPoint;
    @Mock
    private ApplicationMessageService message;
    @Mock
    private FilterChain filterChain;
    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }
    @Test
    void whenLoginPath_InvokeFilterChain() throws ServletException, IOException {
        executeFilterChain(authFilter, request, response, filterChain, "/api/v1/users/login");

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void whenLoginPath_NotInvokeTokenValidation() throws ServletException, IOException {
        executeFilterChain(authFilter, request, response, filterChain, "/api/v1/users/login");

        verify(tokenProvider, never()).validateToken(anyString());
    }
    @Test
    void whenRegisterPath_InvokeFilterChain() throws ServletException, IOException {
        executeFilterChain(authFilter, request, response, filterChain, "/api/v1/users/register");

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void whenRegisterPath_NotInvokeTokenValidation() throws ServletException, IOException {
        executeFilterChain(authFilter, request, response, filterChain, "/api/v1/users/register");

        verify(tokenProvider, never()).validateToken(anyString());
    }

    @Test
    void whenInvalidToken_NotProcessRequest() throws ServletException, IOException {
        simulateInvalidToken(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, "invalid", "/api/v1/users/1");

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void whenInvalidToken_NoSetSecurityContext() throws ServletException, IOException {
        simulateInvalidToken(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, "invalid", "/api/v1/users/1");

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void whenInvalidToken_InvokeEntryPoint() throws IOException, ServletException {
        simulateInvalidToken(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, "invalid", "/api/v1/users/1");

        verify(authEntryPoint).commence(eq(request), eq(response), any(InsufficientAuthenticationException.class));
    }
    @Test
    void whenNoToken_NoProcessRequest() throws ServletException, IOException {
        simulateNoToken(authFilter, request, response, filterChain, cookieRetriever, "/api/v1/users/1");

        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void whenNoToken_InvokeEntryPoint() throws ServletException, IOException {
        simulateNoToken(authFilter, request, response, filterChain, cookieRetriever, "/api/v1/users/1");

        verify(authEntryPoint).commence(eq(request), eq(response), any(InsufficientAuthenticationException.class));
    }
    @Test
    void whenNoToken_NoSetSecurityContext() throws ServletException, IOException {
        simulateNoToken(authFilter, request, response, filterChain, cookieRetriever, "/api/v1/users/1");

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void whenAuthSuccess_FilterChainContinues() throws ServletException, IOException {
       simulateSuccessAuth(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetails, "token", "User", "/api/v1/users/1");

        verify(filterChain).doFilter(request, response);
   }
    @Test
    void whenAuthSuccess_SetSecurityContext() throws ServletException, IOException {
       simulateSuccessAuth(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetails, "token", "User", "/api/v1/users/1");

       assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
   @Test
   void whenNoUser_NoProcessRequest() throws ServletException, IOException {
       simulateMissingUser(authFilter, request, response, filterChain, cookieRetriever,
                tokenProvider, userDetails, "token", "Tom", "/api/v1/users/1");

       verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void whenNoUser_SecurityContextNotSet() {
        simulateMissingUser(authFilter, request, response, filterChain, cookieRetriever, tokenProvider,
                userDetails, "token", "Tom", "/api/v1/users/1");

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
