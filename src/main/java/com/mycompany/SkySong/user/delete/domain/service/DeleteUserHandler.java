package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.user.delete.domain.ports.DeleteUser;

class DeleteUserHandler {

    private final DeleteUser deleteUser;

    public DeleteUserHandler(DeleteUser deleteUser) {
        this.deleteUser = deleteUser;
    }
}