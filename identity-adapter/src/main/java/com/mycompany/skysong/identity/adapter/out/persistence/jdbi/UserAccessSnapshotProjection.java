package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import java.util.Set;

public record UserAccessSnapshotProjection(
        int userId,
        String userTag,
        Set<String> roles) {
}
