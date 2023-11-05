package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteServiceImplTest {
    private DeleteService deleteService;
    @Mock
    private UserDAO userDAO;


}
