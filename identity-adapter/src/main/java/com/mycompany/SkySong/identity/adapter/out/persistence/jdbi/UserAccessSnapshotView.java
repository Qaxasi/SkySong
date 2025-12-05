package com.mycompany.SkySong.identity.adapter.out.persistence.jdbi;

import java.util.Set;

public record UserAccessSnapshotView(int userId, String userTag, Set<String> roles) {
}
