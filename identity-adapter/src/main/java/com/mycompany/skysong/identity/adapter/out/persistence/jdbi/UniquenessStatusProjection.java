package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

public record UniquenessStatusProjection(boolean usernameExists,
                                         boolean emailExists) {}