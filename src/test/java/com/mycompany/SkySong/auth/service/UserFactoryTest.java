package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserFactoryTest {

    @Autowired
    private UserFactory userFactory;

    @Test
    void whenUserCreated_AttributesMatch() {
        RegisterRequest registerRequest = new RegisterRequest("User", "mail@mail.com", "Pass#3");
        Role role = new Role(UserRole.ROLE_USER);

        User user = userFactory.createUser(registerRequest, role);

        assertEquals("User", user.getUsername());
        assertEquals("mail@mail.com", user.getEmail());
        assertNotEquals("Pass#3", user.getPassword());
        assertTrue(user.getRoles().contains(role));
    }
}
