package com.mycompany.SkySong.identity.a.adapter.out.persistence.jdbi;

import java.util.Set;

public record UserAccessSnapshotView(int userId, String userTag, int accessVersion, Set<String> roles) {
}
