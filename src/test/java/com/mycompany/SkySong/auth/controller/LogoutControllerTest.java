package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.security.*;
import com.mycompany.SkySong.auth.security.JwtAuthenticationEntryPoint;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.controller.LoginHelper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.mycompany.SkySong.testsupport.auth.controller.CookieAssertions.assertCookieIsDeleted;
import static com.mycompany.SkySong.testsupport.auth.controller.LogoutControllerTestHelper.*;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class LogoutControllerTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;
    private final String endpoint = "/api/v1/users/logout";
    @Test
    void whenSuccessfulLogout_ReturnStatusOk() throws Exception {
        Cookie mockCookie = mockedTokenCookie();
        assertStatus(mockMvc, endpoint, mockCookie, 200);
    }
    @Test
    void whenSuccessfulLogout_DeleteAuthTokenCookie() throws Exception {
        Cookie mockCookie = mockedTokenCookie();
        assertCookieIsDeleted(mockMvc, endpoint, mockCookie);
    }
    @Test
    void whenLogoutFails_HandleException() throws Exception {
        configureCookieDeleterToThrowException(cookieDeleter);
        assertStatusWithoutCookie(mockMvc, endpoint, 500);
    }
    @Test
    void whenLogoutWithoutCookie_ReturnStatusOk() throws Exception {
        assertStatusWithoutCookie(mockMvc, endpoint,200);
    }

    private Cookie loginAndGetCookie() throws Exception {
        MockHttpServletResponse response =
        mockMvc.perform(post(LoginHelper.loginUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(LoginHelper.validCredentials))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        return response.getCookie("auth_token");
    }
}
