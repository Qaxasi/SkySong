package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.testsupport.controller.DeleteRequestAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteUserControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDAO userDAO;
    @BeforeEach
    void setUp() {
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);

        when(userDAO.findById(1L))
                .thenReturn(Optional.of(new User(1, "testUsername", "testEmail@gmail.com",
                        "$2a$10$VEbWwz6NcL4y6MgKEE/sJuWiFe2EoVbru6gJ.6Miu6G16NWfqlxci", roles)));
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void shouldReceiveOkStatusWhenDeletingUserWithValidId() throws Exception{
        long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 200);
    }
}
