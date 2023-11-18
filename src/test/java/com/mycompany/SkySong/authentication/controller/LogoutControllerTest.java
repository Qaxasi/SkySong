package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.config.SecurityConfig;
import com.mycompany.SkySong.authentication.secutiry.CustomAccessDeniedHandler;
import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.testsupport.controller.CookieAssertions;
import com.mycompany.SkySong.testsupport.controller.PostRequestAssertions;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogoutController.class)
@Import(SecurityConfig.class)
public class LogoutControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Test
    @WithMockUser
    void shouldReturnStatusOkAfterSuccessfulLogout() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        PostRequestAssertions.assertPostStatusReturnsWithoutBody(
                mockMvc,
                "/api/v1/users/logout",
                mockCookie,
                200);
    }
    @Test
    @WithMockUser
    void shouldReturnMessageAfterSuccessfulLogout() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        String expectedMessage = "User logged out successfully";

        PostRequestAssertions.assertMessageReturns(mockMvc, "/api/v1/users/logout", mockCookie, expectedMessage);
    }
    @Test
    @WithMockUser
    void test() throws Exception {
        Cookie mockCookie = new Cookie("auth_token", "token-value");
        CookieAssertions.assertCookieIsDeleted(mockMvc, "/api/v1/users/logout", mockCookie);
    }
    @Test
    @WithMockUser
    void shouldReturnStatusOkWhenLogoutWithoutCookie() throws Exception {
        PostRequestAssertions.assertPostStatusReturnsWithoutBodyAndCookie(mockMvc,
                "/api/v1/users/logout",
                200);
        mockMvc.perform(post("/api/v1/users/logout"))
                .andExpect(status().isOk());
    }
}
