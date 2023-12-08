package com.mycompany.SkySong.testsupport;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class AuthenticationTestHelper {

    public static Cookie loginAndGetCookie(MockMvc mockMvc, String requestBody) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        String jwtToken = JsonPath.parse(responseString).read("$.accessToken");
        return new Cookie("auth_token", jwtToken);
    }
}
