package com.mycompany.SkySong.application.registration.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserRegistrationMapperTest {

    private RoleMapper roleMapper;
    private UserRegistrationMapper userMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapper();
        userMapper = new UserRegistrationMapper(roleMapper);
    }
}
