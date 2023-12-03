package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRoleManagerImplTest {
    @InjectMocks
    private UserRoleManagerImplTest userRoleManagerImplTest;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private ApplicationMessageService messageService;

}
