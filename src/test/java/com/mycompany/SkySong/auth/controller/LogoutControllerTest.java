package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.UriConstants;
import com.mycompany.SkySong.testsupport.auth.controller.LoginRequests;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.mycompany.SkySong.testsupport.UriConstants.LOGOUT_URI;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class LogoutControllerTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenSuccessfulLogout_ReturnStatusOk() throws Exception {
        // given
        Cookie cookie = loginAndGetCookie();

        // when & then
        mockMvc.perform(post(LOGOUT_URI).cookie(cookie))
                .andExpect(status().is(200));
    }
    @Test
    void whenSuccessfulLogout_DeleteAuthTokenCookie() throws Exception {
        // given
        Cookie cookie = loginAndGetCookie();

        // when
        ResultActions logoutResult =
                mockMvc.perform(post(LOGOUT_URI).cookie(cookie))
                        .andExpect(status().isOk());

        MockHttpServletResponse response = logoutResult.andReturn().getResponse();
        Cookie deletedCookie = response.getCookie("auth_token");

        // then
        assertTrue(deletedCookie == null || deletedCookie.getMaxAge() == 0);
    }
}
