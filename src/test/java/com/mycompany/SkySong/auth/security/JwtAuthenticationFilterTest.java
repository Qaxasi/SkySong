package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.TokenGeneratorHelper;
import com.mycompany.SkySong.testsupport.auth.security.InvalidTokenGeneratorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest extends BaseIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenGeneratorHelper tokenGenerator;
    @Autowired
    private InvalidTokenGeneratorHelper invalidTokenGenerator;

}
