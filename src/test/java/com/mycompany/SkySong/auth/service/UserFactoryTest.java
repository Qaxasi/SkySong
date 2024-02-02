package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public class UserFactoryTest {
    @Autowired
    private UserFactory userFactory;

    @Test
    void shouldCreateUserWithGivenDetails() {
        RegisterRequest registerRequest = new RegisterRequest("User", "mail@mail.com", "Pass#3");
        Role role = new Role(UserRole.ROLE_USER);

        User user = userFactory.createUser(registerRequest, role);

        assertEquals("User", user.getUsername());
        assertEquals("mail@mail.com", user.getEmail());
        assertNotEquals("Pass#3", user.getPassword());
        assertTrue(user.getRoles().contains(role));
    }
}
