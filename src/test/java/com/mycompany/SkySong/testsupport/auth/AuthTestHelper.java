package com.mycompany.SkySong.testsupport.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthTestHelper {

    private final MockMvc mockMvc;

    public AuthTestHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public Cookie loginAndGetCookie() throws Exception {
        MockHttpServletResponse response =
                mockMvc.perform(post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(LoginRequests.VALID_CREDENTIALS)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();

        return response.getCookie("session_id");
    }
}
