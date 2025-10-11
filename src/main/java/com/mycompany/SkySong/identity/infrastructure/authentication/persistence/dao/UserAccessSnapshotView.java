package com.mycompany.SkySong.identity.infrastructure.authentication.persistence.dao;

import java.util.Set;

public record UserAccessSnapshotView(int userId, String userTag, int accessVersion, Set<String> roles) {
}
