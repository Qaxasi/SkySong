package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.application.registration.dto.RegisterRequest;
import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.registration.service.UserCreator;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserCreatorTest {

    private InMemoryRoleDAO roleDAO;
    private InMemoryUserDAO userDAO;
    private RegistrationRequests requests;
    private UserCreator userCreator;
    private CustomPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        roleDAO = new InMemoryRoleDAO();
        roleDAO.addDefaultRoles();

        userDAO = new InMemoryUserDAO(roleDAO);

        encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        userCreator = new UserCreator(encoder, roleDAO);

        requests = new RegistrationRequests();
    }

    @AfterEach
    void cleanup() {
        roleDAO.clear();
        userDAO.clear();
    }

    @Test
    void whenGivenRequest_CreateUserWithGivenData() {
        User user = createUser(requests.request("Alex", "alex@mail.com", "Password#3"));

        assertThat(user.getUsername()).isEqualTo("Alex");
        assertThat(user.getEmail()).isEqualTo("alex@mail.com");
        assertThat(encoder.matches("Password#3", user.getPassword())).isTrue();
    }

    @Test
    void whenUserCreated_PasswordIsHashed() {
        User user = createUser(requests.requestWithPassword("Password#3"));
        assertThat(user.getPassword()).isNotEqualTo("Password#3");
    }

    @Test
    void whenUserCreated_UserHasRole() {
        User user = userCreator.createUser(requests.validRequest());
        assertThat(user.getRoles().stream().anyMatch(role -> role.getName().equals(UserRole.ROLE_USER))).isTrue();
    }

    @Test
    void whenDefaultRoleNotFound_ThrowException() {
        roleDAO.clear();
        assertThrows(RoleNotFoundException.class, () -> userCreator.createUser(requests.validRequest()));
    }

    private void deleteRoles() {
        roleDAO.clear();
    }


    private User createUser(RegisterRequest request) {
        return userCreator.createUser(request);
    }
}
