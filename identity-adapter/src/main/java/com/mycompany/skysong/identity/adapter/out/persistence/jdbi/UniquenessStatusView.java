package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

public record UniquenessStatusView(boolean usernameExists, boolean emailExists) {}
