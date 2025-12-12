package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record UserDeletionPrecheckView(
        @ColumnName("user_exists")
        boolean userExists,
        @ColumnName("is_last_admin")
        boolean isLastAdmin) {
}
