package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.domain.registration.service.RequestValidation;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;

class RequestValidationTest {

    private RegistrationRequests requests;
    private InMemoryUserDAO userDAO;
    private InMemoryRoleDAO roleDAO;
    private UserFixture userFixture;
    private RequestValidation validation;
}
