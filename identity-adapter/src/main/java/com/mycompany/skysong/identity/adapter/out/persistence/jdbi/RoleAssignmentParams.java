package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

public record RoleAssignmentParams(
        int userId,
        String roleCode) {
}
