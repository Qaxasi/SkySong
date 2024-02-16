package com.mycompany.SkySong.testsupport;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static com.mycompany.SkySong.testsupport.JsonUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class AuthenticationTestHelper {

    public static Cookie regularUser(MockMvc mockMvc) throws Exception {
        LoginRequest user = new LoginRequest("User", "Password#3");
        return loginAndGetCookie(mockMvc, user);
    }

    public static Cookie adminUser(MockMvc mockMvc) throws Exception {
       LoginRequest admin = new LoginRequest("testAdmin", "Password#3");
        return loginAndGetCookie(mockMvc, admin);
    }

    private static Cookie loginAndGetCookie(MockMvc mockMvc, LoginRequest request) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        return Arrays.stream(mvcResult.getResponse().getCookies())
                .filter(cookie -> "session_id".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Session cookie not found"));
    }
}
