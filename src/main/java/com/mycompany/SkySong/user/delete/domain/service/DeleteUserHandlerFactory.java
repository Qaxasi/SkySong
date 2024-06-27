package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.user.delete.domain.ports.DeleteUser;

public class DeleteUserHandlerFactory {

    public DeleteUserHandler createDeleteUserHandler(DeleteUser deleteUser) {
        return new DeleteUserHandler(deleteUser);
    }
}
