package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.adapter.login.controller.MockTransactionTemplate;
import com.mycompany.SkySong.domain.registration.service.RequestValidation;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

class RequestValidationTest {

    private RegistrationRequests requests;
    private InMemoryUserDAO userDAO;
    private InMemoryRoleDAO roleDAO;
    private UserFixture userFixture;
    private RequestValidation validation;

    @BeforeEach
    void setUp() {
        roleDAO = new InMemoryRoleDAO();
        userDAO = new InMemoryUserDAO(roleDAO);

        roleDAO.addDefaultRoles();

        validation = new RequestValidation(userDAO);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        UserBuilder userBuilder = new UserBuilder(roleDAO, encoder);

        TransactionTemplate transactionTemplate = new MockTransactionTemplate();
        userFixture = new UserFixture(userBuilder, transactionTemplate, userDAO);

        requests = new RegistrationRequests();
    }

    @AfterEach
    void cleanUp() {
        roleDAO.clear();
        userDAO.clear();
    }
}
