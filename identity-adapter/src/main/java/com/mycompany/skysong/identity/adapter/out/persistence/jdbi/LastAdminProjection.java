package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record LastAdminProjection(
        @ColumnName("is_last_admin")
        boolean isLastAdmin) {
}
