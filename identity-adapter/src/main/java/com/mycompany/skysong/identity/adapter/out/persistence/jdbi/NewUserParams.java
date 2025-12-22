package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

public record NewUserParams(
        String username,
        String email,
        String passwordHash,
        String userTag) {}
