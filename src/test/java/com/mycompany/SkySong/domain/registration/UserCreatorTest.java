package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.registration.service.UserCreator;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testutils.data.UserRegistrationData;
import com.mycompany.SkySong.testutils.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserCreatorTest {

    private InMemoryRoleDAO roleDAO;
    private InMemoryUserDAO userDAO;
    private UserRegistrationData registrationData;
    private UserCreator userCreator;
    private CustomPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        roleDAO = new InMemoryRoleDAO();

        userDAO = new InMemoryUserDAO(roleDAO);

        encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        userCreator = new UserCreator(encoder, roleDAO);

        registrationData = new UserRegistrationData();
    }

    @AfterEach
    void cleanup() {
        roleDAO.clear();
        userDAO.clear();
    }

    @Test
    void whenGivenRequest_CreateUserWithGivenData() {
        User user = createUser(registrationData.with("Alex", "alex@mail.com", "Password#3"));

        assertThat(user.getUsername()).isEqualTo("Alex");
        assertThat(user.getEmail()).isEqualTo("alex@mail.com");
        assertPasswordMatches("Password#3", user.getPassword());
    }

    @Test
    void whenUserCreated_PasswordIsHashed() {
        User user = createUser(registrationData.requestWithPassword("Password#3"));
        assertThat(user.getPassword()).isNotEqualTo("Password#3");
    }

    @Test
    void whenUserCreated_UserHasRole() {
        User user = createUser(registrationData.validData());
        assertThat(user.getRoles().stream().anyMatch(role -> role.getName().equals(UserRole.ROLE_USER))).isTrue();
    }

    @Test
    void whenDefaultRoleNotFound_ThrowException() {
        deleteRoles();
        assertThrows(RoleNotFoundException.class, () -> createUser(registrationData.validData()));
    }

    private void deleteRoles() {
        roleDAO.clear();
    }

    private User createUser(UserRegistrationDto registrationDto) {
        return userCreator.createUser(registrationDto);
    }

    private void assertPasswordMatches(String password, String encodedPassword) {
        assertThat(encoder.matches(password, encodedPassword)).isTrue();
    }
}



