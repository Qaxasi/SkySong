package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.TokenGeneratorHelper;
import com.mycompany.SkySong.testsupport.auth.controller.LoginRequests;
import com.mycompany.SkySong.testsupport.auth.controller.RegistrationRequests;
import com.mycompany.SkySong.testsupport.auth.security.InvalidTokenGeneratorHelper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static com.mycompany.SkySong.testsupport.UriConstants.LOGOUT_URI;
import static com.mycompany.SkySong.testsupport.UriConstants.REGISTRATION_URI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenGeneratorHelper tokenGenerator;
    @Autowired
    private InvalidTokenGeneratorHelper invalidTokenGenerator;
    @Test
    void whenInvalidToken_ThenUnauthorized() throws Exception {
        // given
        String token = invalidTokenGenerator.generateExpiredToken();

        // when & then
        mockMvc.perform(post(LOGOUT_URI).cookie(new Cookie("auth_cookie", token)))
                .andExpect(status().isUnauthorized());

    }
    @Test
    void whenNoToken_ThenUnauthorized() throws Exception {
        // when & then
        mockMvc.perform(post(LOGOUT_URI))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenLoginPath_ThenBypassSecurityFilter() throws Exception {
        // given
        LoginRequest request = LoginRequests.VALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(200));
    }
    @Test
    @Transactional
    void whenRegistrationPath_ThenBypassSecurityFilter() throws Exception {
        // given
        RegisterRequest request = RegistrationRequests.VALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post(REGISTRATION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(201));
    }
}

