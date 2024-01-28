package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.TokenGeneratorHelper;
import com.mycompany.SkySong.testsupport.auth.controller.LoginRequests;
import com.mycompany.SkySong.testsupport.auth.security.InvalidTokenGeneratorHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
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
    void whenLoginPath_ThenBypassSecurityFilter() throws Exception {
        // given
        LoginRequest request = LoginRequests.VALID_CREDENTIALS;

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().is(200));
    }
}
