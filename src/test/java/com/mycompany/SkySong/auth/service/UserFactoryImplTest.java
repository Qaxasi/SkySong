package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public class UserFactoryImplTest {

    private UserFactoryImpl userFactory;

    private PasswordService passwordService;
    @Test
    void shouldCreateUserWithGivenDetails() {
        String username = "testUsername";
        String email = "testEmail@gmail.com";
        String password = "testPassword@123";
        String encodedPassword = "encodedPassword";
        Role role = new Role(UserRole.ROLE_USER);

        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(passwordService.encodePassword(password)).thenReturn(encodedPassword);

        User user = userFactory.createUser(registerRequest, role);

        assertEquals(user.getUsername(), registerRequest.username());
        assertEquals(user.getEmail(), registerRequest.email());
        assertEquals(user.getPassword(), encodedPassword);
        assertTrue(user.getRoles().contains(role));
    }
}
