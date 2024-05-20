package com.mycompany.SkySong.user.delete.domain.service;

import org.springframework.stereotype.Service;

@Service
public class DeleteUserFacade {

    private final DeleteUserHandler deleteUser;

    public DeleteUserFacade(DeleteUserHandler deleteUser) {
        this.deleteUser = deleteUser;
    }
}
