package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.domain.registration.service.UserCreator;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
}
