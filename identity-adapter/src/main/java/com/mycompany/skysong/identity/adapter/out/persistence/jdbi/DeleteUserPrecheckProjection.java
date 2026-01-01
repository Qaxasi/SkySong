package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

public record DeleteUserPrecheckProjection(
        boolean userExists,
        boolean isAdmin,
        boolean otherAdminExists) {
}
