package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

public record UserInsertRow(String username,
                            String email,
                            String password,
                            String userTag) {}
