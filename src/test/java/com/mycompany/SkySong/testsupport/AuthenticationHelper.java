package com.mycompany.SkySong.testsupport;

import com.mycompany.SkySong.testsupport.auth.controller.LoginRequests;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static com.mycompany.SkySong.testsupport.UriConstants.LOGIN_URI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class AuthenticationHelper {
    @Autowired
    private MockMvc mockMvc;
    public Cookie loginAndGetCookie() throws Exception {
        MockHttpServletResponse response =
                mockMvc.perform(post(LOGIN_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(LoginRequests.VALID_CREDENTIALS)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();

        return response.getCookie("auth_token");
    }
}
