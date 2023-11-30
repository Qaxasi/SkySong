package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CredentialExistenceCheckerImplTest {
    @InjectMocks
    private CredentialExistenceCheckerImpl credentialExistenceChecker;
    @Mock
    private UserDAO userDAO;
    @Mock
    private ApplicationMessageService messageService;
}
