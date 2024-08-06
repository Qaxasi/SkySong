package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.domain.registration.service.UserCreator;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;

class UserCreatorTest {

    private InMemoryRoleDAO roleDAO;
    private InMemoryUserDAO userDAO;
    private RegistrationRequests requests;
    private UserCreator userCreator;

}
