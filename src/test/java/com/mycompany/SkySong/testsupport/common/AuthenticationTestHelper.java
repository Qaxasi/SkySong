package com.mycompany.SkySong.testsupport.common;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static com.mycompany.SkySong.testsupport.common.JsonUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class AuthenticationTestHelper {

    private final MockMvc mockMvc;

    public AuthenticationTestHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public Cookie loginRegularUser() {
        LoginRequest user = new LoginRequest("User", "Password#3");
        return loginAndGetCookie(user);
    }

    public Cookie loginAdminUser() {
       LoginRequest admin = new LoginRequest("testAdmin", "Password#3");
        return loginAndGetCookie(admin);
    }

    public void logoutUser(Cookie sessionCookie) {
        try {
            mockMvc.perform(post("/api/v1/users/logout").cookie(sessionCookie))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException("Logout failed " + e.getMessage());
        }
    }

    public Cookie loginAndGetCookie(LoginRequest request) {
        try {
            MvcResult mvcResult = mockMvc.perform(post("/api/v1/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(request)))
                    .andExpect(status().isOk())
                    .andReturn();

            return Arrays.stream(mvcResult.getResponse().getCookies())
                    .filter(cookie -> "session_id".equals(cookie.getName()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Session cookie not found"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to login for user " + request.usernameOrEmail());
        }
    }
}
